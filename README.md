# proto3docgen

This is designed to be used in a bazel repo to build various forms of API documentation for a set of protobufs. The only part fully built out now is aimed at markdown that we've tested with hugo.


## How to use
We auto generate releases that include a bash script which is designed to be checked into the repo. It can be ran directly, but currently (we could move to a config file) we wrap it in a script we check in/maintain in our repos to configure things like the command line args and how you want to use that script. We recommend you just copy the script from the relase into your repo, at a path like `scripts/run_doc_generation.sh` and then customize the wrapping bash script.



```sh
#!/bin/bash

set -e
SCRIPTS_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $SCRIPTS_DIR
REPO_ROOT=$(git rev-parse --show-toplevel)
DOCS_PATH="$REPO_ROOT/path/to/your/docs"
PATH_TO_RELEASE_SCRIPT="$REPO_ROOT/scripts/run_doc_generation.sh"

cd $REPO_ROOT
rm -rf $DOCS_PATH
$PATH_TO_RELEASE_SCRIPT -g https://github.com/myteam/myproject -o $DOCS_PATH --rendering-engine Hugo

cat >$DOCS_PATH/_index.md <<EOL

---
title: "Protobuf auto-generated API documentation"
linkTitle: "Protobuf API"
menu:
  main:
    weight: 30
---

EOL
```

## Authors

1. siddarth
2. ianoc
