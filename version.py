#!/usr/bin/env python3
import glob
import os
import subprocess
import sys
from typing import List


def die(msg: str):
    print(msg, file=sys.stderr)
    sys.exit(1)


def cmd_exec(cmd: List[str]) -> str:
    result = subprocess.run(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
    if result.returncode != 0:
        die(f"Error: Failed to execute command: {' '.join(cmd)}\n{result.stderr}")
    return result.stdout.strip()


def check_git_status():
    stdout = cmd_exec(["git", "status", "--porcelain"])
    if len(stdout) > 0:
        die("Error: There are uncommitted changes. Please commit or stash them before proceeding.")

    stdout = cmd_exec(["git", "tag", "--points-at", "HEAD"])
    if len(stdout) > 0:
        die("Error: HEAD is already tagged.")

    branch = cmd_exec(["git", "branch", "--show-current"])
    if branch != "main":
        die("Error: You must be on the main branch to update the version.")


def get_version_from_pom_xml(pom_file: str) -> str:
    with open(pom_file, "r") as f:
        lines = f.readlines()

    version = None

    for line in lines:
        if "<version>" in line:
            version = line.strip().replace("<version>", "").replace("</version>", "").strip()
            break

    if version is None:
        die(f"Error: Could not find version in {pom_file}.")

    return version


def set_version_in_pom_xml(pom_file: str, version: str):
    with open(pom_file, "r") as f:
        lines = f.readlines()

    for i, line in enumerate(lines):
        if "<version>" in line:
            lines[i] = line[:line.find("<version>")] + f"<version>{version}</version>\n"
            break

    with open(pom_file, "w") as f:
        f.writelines(lines)


def update_version(part: str) -> str:
    # search all pom.xml files
    pom_file = os.path.join(os.path.dirname(__file__), "pom.xml")

    # Update version
    version = get_version_from_pom_xml(pom_file)

    major, minor, patch = version.split(".")
    if part == "major":
        major = str(int(major) + 1)
        minor = "0"
        patch = "0"
    elif part == "minor":
        minor = str(int(minor) + 1)
        patch = "0"
    elif part == "patch":
        patch = str(int(patch) + 1)
    else:
        die("Error: Invalid version part.")

    new_version = f"{major}.{minor}.{patch}"

    set_version_in_pom_xml(pom_file, new_version)

    return new_version


def git_tag(version: str):
    cmd_exec(["git", "add", "."])
    cmd_exec(["git", "commit", "-m", f"v{version}"])
    cmd_exec(["git", "tag", "-a", f"v{version}", "-m", f"v{version}"])


def main():
    args = sys.argv[1:]

    if len(args) != 1 or args[0] not in ["major", "minor", "patch"]:
        print("Usage: python3 version [major|minor|patch]")
        sys.exit(1)

    check_git_status()
    version = update_version(args[0])
    git_tag(version)

    print(f"Tag v{version} created.")


if __name__ == '__main__':
    main()
