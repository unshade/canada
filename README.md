# CanAda
"Because if you can, you canada", Sir. Bouthier.

Ada Compiler from scratch. No lib, no doc, only us and our keyboard. Compiles ada files to ASM ARM 32 UAL (raspberry compatible).

Follow every compilation steps with logs and graphs.

# Setup

## Java setup

JDK 17 is required to build the project.
Gradle is used as build tool.

### Install JDK 17

```bash
brew install openjdk@17
```

### Install Gradle

```bash
brew install gradle
```

## Setup python virtual environment

### Graphviz

```bash
brew install graphviz
export GRAPHVIZ_DIR="$(brew --prefix graphviz)"
pip install pygraphviz \
    --config-settings=--global-option=build_ext \
    --config-settings=--global-option="-I$GRAPHVIZ_DIR/include" \
    --config-settings=--global-option="-L$GRAPHVIZ_DIR/lib"
```
### Python

```bash
cd <project_dir>/python
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

# Build and run

## Build

```bash
gradle build
```

## Run

```bash
gradle run --args="test.canAda -g"
```
