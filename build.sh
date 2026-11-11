#!/bin/sh
# Compila o projeto e gera o JAR executavel (Linux/Mac).
# Uso:  ./build.sh        (precisa do JDK: javac e jar)
# Se a classe principal mudar de nome, edite MAIN_CLASS abaixo.
MAIN_CLASS="${MAIN_CLASS:-locadora.ui.JanelaPrincipal}"

cd "$(dirname "$0")" || exit 1
MAIN_FONTE="src/$(echo "$MAIN_CLASS" | tr . /).java"
if [ ! -f "$MAIN_FONTE" ]; then
    echo "Classe principal nao encontrada: $MAIN_FONTE"
    exit 1
fi

rm -rf bin && mkdir bin
# -sourcepath src: o javac compila tambem tudo o que a classe principal usa,
# mesmo que esteja em pacotes novos.
javac -encoding UTF-8 -sourcepath src -d bin "$MAIN_FONTE" || exit 1

printf 'Main-Class: %s\n' "$MAIN_CLASS" > manifest.txt
jar cfm Locadora.jar manifest.txt -C bin . || exit 1
rm -f manifest.txt
echo "Gerado: Locadora.jar   (execute com: java -jar Locadora.jar)"
