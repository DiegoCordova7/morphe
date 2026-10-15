let editor;

const SAMPLE_CODE = `func main() : Void {
  age : Integer = 25;
  match (age) {
    0 .. 17 => print("Minor");
    18 .. 64 => print("Adult");
    _ => print("Senior");
  }
}`;

export function initEditor() {
  editor = CodeMirror.fromTextArea(document.getElementById("source-code"), {
    lineNumbers: true,
    theme: "morphe",
    mode: "morphe",
    indentUnit: 2,
    tabSize: 2,
    indentWithTabs: false,
    extraKeys: {
      Tab: (cm) => cm.execCommand("insertSoftTab"),
    },
  });
}

export function getEditorValue() {
  return editor.getValue();
}

export function loadSampleCode() {
  editor.setValue(SAMPLE_CODE);
}