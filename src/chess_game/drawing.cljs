(ns chess-game.drawing
  (:require [clj-di.core :refer [get-dep]]
            [chess-game.graphics :as graphics]
            [chess-game.images :as images]
            [chess-game.config :as config]))

(defn gray-scale
  [scale]
  "Sets the fill color to a gray scale, values ranging between 0 and 255"
  (graphics/make-color scale scale scale))

(defn light-blue
  [scale]
  "A light blue color, designed for clicking on a white square."
  (graphics/make-color (- scale 50) (- scale 50) scale))

(defn light-red
  [scale]
  "A light red color, designed showing the next legal move.."
  (graphics/make-color scale (- scale 50) (- scale 50)))


(defn dark-blue
  [scale]
  "A dark blue color, designed for clicking on a black square."
  (graphics/make-color scale scale (+ scale 50)))


(defn dark-red
  [scale]
  "A dark red color, designed for showing the next legal move."
  (graphics/make-color (+ scale 50) scale scale 50))


(defn draw-chessmens!
  []
  "Draw all the chessmen"
  (doseq [[pos chessman] (:chessboard @(get-dep :env))]
    (when chessman
      (images/draw-chessman! chessman pos))))

(defn white-legal-next-move
  []
  "Square is legal next move"
  (light-red 200))

(defn black-legal-next-move
  []
  "Square is legal next move"
  (dark-red 50))


(defn white-default
  []
  "Default white color"
  (gray-scale 200))

(defn black-default
  []
  "Default white color"
  (gray-scale 50))

(defn white-selected
  []
  (light-blue 255))

(defn black-selected
  []
  (dark-blue 50))

(defn tile-fill
  [color selected legal-next-move]
  "Fill the tile based on color (:white or :black) and whether the tile is currently selected.
This function will probably take a symbol in the future, with a limited set of valid symbols
that the square can be in. For example, the square could be:

  * Tinted green to show that the currently selected piece can move into this square.
  * Tinted red to show that moving your piece there would leave you vulnerable to being
    captured.
  * Tinted purple for a move that would put you into check.
  * Tinted yellow when the computer is thinking about placing a piece.

There are probably lots more, but those are just some of them that came off the top of my head.
"
  (condp = [color selected legal-next-move]
    [:white false true] (white-legal-next-move)
    [:black false true] (black-legal-next-move)

    [:white true  false] (white-selected)
    [:black true  false] (black-selected)

    [:white false false] (white-default)
    [:black false false] (black-default)
    ))

(defn square-should-be-white?
  [x y]
  "Return true if the square at (x, y) should be white"
  (even? (+ x y)))

(defn square-color
  [x y]
  (if (square-should-be-white? x y)
    :white
    :black))

(defn tile-fill-color
  [x y selected legal-next-move]
  (tile-fill (square-color x y) selected legal-next-move))

(defn get-color-from
  [i j]
  (let [
        env (get-dep :env)
        selected (= [i j] (:selected-tile @env))
        legal-next-move (boolean (some #(= [i j] %) (:legal-next-moves @env)))
        ;; legal-next-move (contains? (:legal-next-moves @env) [i j])
        ]

    (tile-fill-color i j selected legal-next-move)))


(defn show-legal-moves
  []
  (let [env (get-dep :env)]
    (println "Legal next moves are " (:legal-next-moves @env))))

(defn draw-checkered-board
  []
  "Draw the checkered board that all chess games are played on"
  (let [size config/tile-size]
    ;; Iterate both i & j over the sequence 0,1,2..9
    (doseq [i (range 0 config/board-tiles-x)
            j (range 0 config/board-tiles-y)]
      (graphics/set-fill! (get-color-from i j))
      (graphics/fill-rect (* i size) (* j size) size size))))
