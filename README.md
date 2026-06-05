````markdown
# LexiAssist

LexiAssist is a Java 17 desktop writing assistant built with JavaFX. It provides autocomplete and spelling suggestions while typing, using dictionary-based lookup structures.

## Features

- Load text from a `.txt` file
- Clear editor content
- Autocomplete suggestions while typing
- Spelling suggestions for misspelled words
- Adjacent-swap spelling correction, such as `recieve` → `receive`
- Keyboard-friendly suggestion popup
- Right-side suggestions panel
- Unit-tested dictionary and suggestion logic

## Keyboard Controls

When the suggestion popup is visible:

| Key | Action |
|---|---|
| `Arrow Down` | Move to next suggestion |
| `Arrow Up` | Move to previous suggestion |
| `Enter` | Apply selected suggestion |
| `Tab` | Apply selected suggestion |
| `Esc` | Close the suggestion popup |

## Technology Stack

- Java 17
- Maven
- JavaFX
- FXML
- JUnit 5

## Project Structure

```text
src/main/java/com/farshad/lexiassist/
├── LexiAssistApplication.java
├── autocomplete/
│   └── AutocompleteService.java
├── dictionary/
│   ├── DictionaryBundle.java
│   ├── DictionaryDataLoader.java
│   ├── autocompletelookup/
│   │   ├── PrefixDictionary.java
│   │   └── PrefixNode.java
│   └── exactwordlookup/
│       ├── HashWordRepository.java
│       └── WordRepository.java
├── editor/
│   └── CurrentWord.java
├── io/
│   └── TextFileLoader.java
├── spelling/
│   └── SpellingSuggestionService.java
└── ui/
    └── EditorController.java
````

## How to Run

Make sure Java 17 and Maven are installed.
Check Java:
```bash
java -version
```

Check Maven:
```bash
mvn -version
```

Run the application:
```bash
mvn clean javafx:run
```

## How to Run Tests

```bash
mvn clean test
```

## Example Words to Try

Autocomplete examples:
```text
pro
hel
app
dev
```

Spelling suggestion examples:
```text
helo
wurld
recieve
```

## Design Notes

LexiAssist uses two dictionary lookup structures:

| Component            | Data structure                                               | Purpose                |
| -------------------- | ------------------------------------------------------------ | ---------------------- |
| `HashWordRepository` | `HashSet<String>`                                            | Fast exact-word lookup |
| `PrefixDictionary`   | Trie-like prefix tree using `HashMap<Character, PrefixNode>` | Autocomplete lookup    |

The exact-word dictionary is useful for checking whether a word exists. The prefix dictionary is useful for finding words that start with the current typed prefix.

The spelling suggestion service generates correction candidates using:

* deletion
* insertion
* substitution
* adjacent character swap

The adjacent-swap operation helps with common typing mistakes such as:

```text
recieve -> receive
```

## Project Note

LexiAssist is an independent portfolio project built to practice Java, JavaFX, data structures, and desktop application design. It was inspired by general text-processing and dictionary concepts from Java learning material, but the implementation, UI, package structure, class design, method names, tests, and project history were created separately.

## Status

Current version:

* Core JavaFX UI is working
* Text loading and clearing are working
* Autocomplete suggestions are working
* Spelling suggestions are working
* Keyboard suggestion popup is working
* Unit tests are included

Planned improvements:

* Add GitHub Actions CI
* Add Docker support for reproducible Maven builds

````