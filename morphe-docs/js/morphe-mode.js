CodeMirror.defineSimpleMode("morphe", {
  start: [
    { regex: /\/\/.*/, token: "comment" },
    { regex: /"(?:[^\\"]|\\.)*"?/, token: "string" },
    {
      regex:
        /(?:type|func|match|new|if|else|while|do|for|attempt|onError|return|true|false|null)\b/,
      token: "keyword",
    },
    { regex: /\b\d+(?:\.\d+)?\b/, token: "number" },
    {
      regex: /=>|==|!=|<=|>=|\+=|-=|\*=|\/=|%=|\+\+|--|\.\.|~=|[+\-*/%=<>!|]/,
      token: "operator",
    },
    { regex: /[{}()[\];:,.#]/, token: "punctuation" },
    { regex: /(?<=\.)[a-z_][\w]*/, token: "def" },
    { regex: /[a-z_][\w]*(?=\s*\()/, token: "def" },
    { regex: /[A-Z][\w]*/, token: "type" },
    { regex: /[a-z_$][\w$]*/, token: "variable" },
  ],
  meta: {
    lineComment: "//",
  },
});
