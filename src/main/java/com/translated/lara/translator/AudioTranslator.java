package com.translated.lara.translator;

import com.translated.lara.errors.LaraApiException;
import com.translated.lara.errors.LaraException;
import com.translated.lara.errors.S3Exception;
import com.translated.lara.errors.TimeoutException;
import com.translated.lara.net.HttpParams;
import com.translated.lara.net.LaraClient;
import com.translated.lara.net.S3Client;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * High-level client for translating audio files using the LARA API.
 * <p>
 * This class encapsulates the full flow of uploading to S3, starting the translation,
 * polling the status, and downloading the translated result.
 */
public class AudioTranslator {
    private final LaraClient client;
    private final S3Client s3Client;

    /**
     * Creates a new {@code AudioTranslator} using the provided LARA HTTP client.
     *
     * @param client an instance of {@link LaraClient} configured with credentials and API endpoint
     */
    public AudioTranslator(LaraClient client) {
        this.client = client;
        this.s3Client = new S3Client();
    }

    /**
     * Uploads an audio file and creates a translation request for the target language.
     *
     * @param file   input audio file
     * @param target ISO language code of the target language (for example {@code "en"}, {@code "it"})
     * @return an {@link Audio} object representing the created translation request
     * @throws LaraException if an error occurs on the LARA API side
     * @throws S3Exception   if an error occurs while uploading to S3
     */
    public Audio upload(File file, String target) throws LaraException, S3Exception {
        return upload(file, null, target, null);
    }

    /**
     * Uploads an audio file specifying both source and target languages.
     *
     * @param file   input audio file
     * @param source ISO language code of the source language; may be {@code null} for auto-detection
     * @param target ISO language code of the target language
     * @return an {@link Audio} object representing the created translation request
     * @throws LaraException if an error occurs on the LARA API side
     * @throws S3Exception   if an error occurs while uploading to S3
     */
    public Audio upload(File file, String source, String target) throws LaraException, S3Exception {
        return upload(file, source, target, null);
    }

    /**
     * Uploads an audio file with advanced translation options.
     *
     * @param file    input audio file
     * @param source  ISO language code of the source language; may be {@code null} for auto-detection
     * @param target  ISO language code of the target language
     * @param options audio upload and translation options; may be {@code null}
     * @return an {@link Audio} object representing the created translation request
     * @throws LaraException if an error occurs on the LARA API side
     * @throws S3Exception   if an error occurs while uploading to S3
     */
    public Audio upload(File file, String source, String target, AudioUploadOptions options) throws LaraException, S3Exception {
        S3UploadParams s3Params = client.get("/v2/audio/upload-url?" + new HttpParams<String>()
                .set("filename", file.getName())
                .toQueryString()
        ).as(S3UploadParams.class);

        s3Client.upload(s3Params.getUrl(), s3Params.getFields(), file);

        String s3key = s3Params.getS3Key();

        HttpParams<Object> params = (options == null ? new HttpParams<>() : options.toParams())
                .set("target", target)
                .set("source", source)
                .set("s3key", s3key);

        Map<String, String> headers = new HashMap<>();
        if (options != null && Boolean.TRUE.equals(options.getNoTrace())) {
            headers.put("X-No-Trace", "true");
        }

        return client.post("/v2/audio/translate", params.build(), null, headers).as(Audio.class);
    }

    /**
     * Retrieves the current status of an audio translation request.
     *
     * @param id identifier of the audio resource returned during upload
     * @return the {@link Audio} object updated with the current status
     * @throws LaraException if an error occurs on the LARA API side
     */
    public Audio status(String id) throws LaraException {
        return client.get("/v2/audio/" + id).as(Audio.class);
    }

    /**
     * Downloads the result of an audio translation as a stream.
     *
     * @param id identifier of the audio resource returned during upload or translation
     * @return an {@link InputStream} from which the translated audio content can be read
     * @throws LaraException if an error occurs on the LARA API side
     * @throws S3Exception   if an error occurs while downloading from S3
     */
    public InputStream download(String id) throws LaraException, S3Exception {
        String downloadUrl = client.get("/v2/audio/" + id + "/download-url")
                .as(Map.class)
                .get("url")
                .toString();

        return s3Client.download(downloadUrl);
    }

    /**
     * Performs a synchronous translation of an audio file: uploads the file, waits for completion,
     * and downloads the result.
     *
     * @param input  input audio file
     * @param target ISO language code of the target language
     * @return an {@link InputStream} with the translated audio content
     * @throws LaraException       if an error occurs on the LARA API side
     * @throws S3Exception         if an error occurs during upload/download on S3
     * @throws InterruptedException if the thread is interrupted while polling
     */
    public InputStream translate(File input, String target) throws LaraException, S3Exception, InterruptedException {
        return translate(input, null, target, null);
    }

    /**
     * Performs a synchronous translation of an audio file specifying source and target languages.
     *
     * @param input  input audio file
     * @param source ISO language code of the source language; may be {@code null} for auto-detection
     * @param target ISO language code of the target language
     * @return an {@link InputStream} with the translated audio content
     * @throws LaraException       if an error occurs on the LARA API side
     * @throws S3Exception         if an error occurs during upload/download on S3
     * @throws InterruptedException if the thread is interrupted while polling
     */
    public InputStream translate(File input, String source, String target) throws LaraException, S3Exception, InterruptedException {
        return translate(input, source, target, null);
    }

    /**
     * Performs a synchronous translation of an audio file with advanced options.
     * <p>
     * This method uploads the file, periodically polls the status until completion or timeout,
     * and finally downloads the translated result.
     *
     * @param input   input audio file
     * @param source  ISO language code of the source language; may be {@code null} for auto-detection
     * @param target  ISO language code of the target language
     * @param options audio upload and translation options; may be {@code null}
     * @return an {@link InputStream} with the translated audio content
     * @throws LaraException       if an error occurs on the LARA API side
     * @throws S3Exception         if an error occurs during upload/download on S3
     * @throws InterruptedException if the thread is interrupted while polling
     */
    public InputStream translate(File input, String source, String target, AudioUploadOptions options) throws LaraException, S3Exception, InterruptedException {
        Audio audio = upload(input, source, target, options);
        audio = pollAudioUntilCompleted(audio);
        return download(audio.getId());

    }

    /**
     * Periodically polls an audio resource until the translation is completed
     * or an error/timeout occurs.
     *
     * @param audio initial {@link Audio} object obtained after upload
     * @return the {@link Audio} object updated to state {@link Audio.Status#TRANSLATED}
     * @throws InterruptedException if the thread is interrupted while polling
     * @throws LaraApiException     if the API returns an error status for the audio resource
     * @throws TimeoutException     if the translation does not finish within the maximum allowed time
     */
    private Audio pollAudioUntilCompleted(Audio audio) throws InterruptedException, LaraException {
        int pollingInterval = 2000;
        int maxWaitTime = 1000 * 60 * 15; // 15 minutes
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < maxWaitTime) {
            Thread.sleep(pollingInterval);
            audio = status(audio.getId());

            if (audio.getStatus() == Audio.Status.ERROR) {
                throw new LaraApiException(500, "AudioError", audio.getErrorReason());
            }

            if (audio.getStatus() == Audio.Status.TRANSLATED) {
                return audio;
            }
        }

        throw new TimeoutException();
    }
}
