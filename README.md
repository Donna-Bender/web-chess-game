# web-chess-game

ClojureScript plus HTML5 canvas = Chess Game that should be relatively platform independent.

## Testing
For using code after this changes you need bower:

```shell
sudo npm install -g bower
```

And install assets from bower:

```shell
lein bower install
```

For running tests:

```shell
lein cljsbuild test
```

For running all tests:

```bash
./scripts/run_tests.sh
```
