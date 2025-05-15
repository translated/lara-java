package com.translated.lara.translator;

import com.translated.lara.errors.LaraApiException;
import com.translated.lara.errors.LaraException;
import com.translated.lara.errors.S3Exception;
import com.translated.lara.errors.TimeoutException;
import com.translated.lara.net.HttpParams;
import com.translated.lara.net.LaraClient;
import com.translated.lara.net.S3Client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Documents {

    private final LaraClient client;
    private final S3Client s3Client;

    public Documents(LaraClient client) {
        this.client = client;
        this.s3Client = new S3Client();
    }

    public Document upload(File file, String target) throws LaraException, S3Exception {
        return upload(file, null, target, null);
    }

    public Document upload(File file, String source, String target) throws LaraException, S3Exception {
        return upload(file, source, target, null);
    }

    /***
     * Uploads a file to be translated to the server and starts the translation process.
     * @param file the input file to be translated
     * @param source the source language (optional)
     * @param target the target language
     * @param options the translation options (optional)
     * @return the Document object representing the translation job
     * @throws LaraException
     */
    public Document upload(File file, String source, String target, DocumentUploadOptions options) throws LaraException, S3Exception {
        S3UploadParams s3Params = client.get("/documents/upload-url", new HttpParams<>()
                .set("filename", file.getName())
                .build()
        ).as(S3UploadParams.class);

        s3Client.upload(s3Params.getUrl(), s3Params.getFields(), file);

        String s3key = s3Params.getS3Key();

        HttpParams<Object> params = (options == null ? new HttpParams<>() : options.toParams())
                .set("target", target)
                .set("source", source)
                .set("s3key", s3key);

        return client.post("/documents", params.build()).as(Document.class);
    }

    /***
     * Checks the status of a document.
     * @param id the document ID
     * @return the document object
     * @throws LaraException
     */
    public Document status(String id) throws LaraException {
        return client.get("/documents/" + id).as(Document.class);
    }

    public InputStream download(String id) throws S3Exception, LaraException {
        return download(id, null);
    }

    /***
     * Downloads the translated document.
     * @param id the document ID
     * @return an InputStream to read the downloaded document
     * @throws IOException
     * @throws LaraException
     */
    public InputStream download(String id, DocumentDownloadOptions options) throws S3Exception, LaraException {
        HttpParams<Object> params = options == null ? null : options.toParams();

        String downloadUrl = client.get("/documents/" + id + "/download-url", params != null ? params.build() : null)
                .as(Map.class)
                .get("url")
                .toString();

        return s3Client.download(downloadUrl);
    }

    public InputStream translate(File input, String target) throws S3Exception, LaraException, InterruptedException {
        return translate(input, null, target, null);
    }

    public InputStream translate(File input, String source, String target) throws S3Exception, LaraException, InterruptedException {
        return translate(input, source, target, null);
    }

    /***
     * Translates a document and returns an InputStream to read the translated document.
     * @param input the input file to be translated
     * @param source the source language (optional)
     * @param target the target language
     * @param options the translation options (optional)
     * @return an InputStream to read the translated document
     * @throws IOException
     * @throws LaraException
     */
    public InputStream translate(File input, String source, String target, DocumentTranslateOptions options) throws S3Exception, LaraException, InterruptedException {
        DocumentUploadOptions uploadOptions = options != null ? new DocumentUploadOptions() : null;
        if (options != null && options.getAdaptTo() != null) uploadOptions.setAdaptTo(options.getAdaptTo());

        Document document = upload(input, source, target, uploadOptions);
        document = pollDocumentUntilCompleted(document);

        DocumentDownloadOptions downloadOptions = options != null ? new DocumentDownloadOptions() : null;
        if (options != null && options.getOutputFormat() != null) downloadOptions.setOutputFormat(options.getOutputFormat());

        return download(document.getId(), downloadOptions);
    }

    private Document pollDocumentUntilCompleted(Document document) throws InterruptedException, LaraException {
        int pollingInterval = 2000;
        int maxWaitTime = 1000 * 60 * 15; // 15 minutes
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < maxWaitTime) {
            Thread.sleep(pollingInterval);
            document = status(document.getId());

            if (document.getStatus() == Document.Status.ERROR) {
                throw new LaraApiException(500, "DocumentError", document.getErrorReason());
            }

            if (document.getStatus() == Document.Status.TRANSLATED) {
                return document;
            }
        }

        throw new TimeoutException();
    }
}
