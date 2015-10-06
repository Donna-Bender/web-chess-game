(ns ^:figwheel-always chess-game.board
  (:require [chess-game.moves :as m]
            [clj-di.core :refer [get-dep register!]]
            [chess-game.pieces :as p]
            ))

(defn make-standard-board
  []
  "Standard chess board implemented as a hash table"
  (hash-map
    ;; Black pieces
    '(0 0) (p/make :black :rook 0 0)
    '(1 0) (p/make :black :knight 1 0)
    '(2 0) (p/make :black :bishop 2 0)
    '(3 0) (p/make :black :queen 3 0)
    '(4 0) (p/make :black :king 4 0)
    '(5 0) (p/make :black :bishop 5 0)
    '(6 0) (p/make :black :knight 6 0)
    '(7 0) (p/make :black :rook 7 0)

    ;; Black pawns
    '(0 1) (p/make :black :pawn 0 1)
    '(1 1) (p/make :black :pawn 1 1)
    '(2 1) (p/make :black :pawn 2 1)
    '(3 1) (p/make :black :pawn 3 1)
    '(4 1) (p/make :black :pawn 4 1)
    '(5 1) (p/make :black :pawn 5 1)
    '(6 1) (p/make :black :pawn 6 1)
    '(7 1) (p/make :black :pawn 7 1)

    ;; White pawns
    '(0 6) (p/make :white :pawn 0 6)
    '(1 6) (p/make :white :pawn 1 6)
    '(2 6) (p/make :white :pawn 2 6)
    '(3 6) (p/make :white :pawn 3 6)
    '(4 6) (p/make :white :pawn 4 6)
    '(5 6) (p/make :white :pawn 5 6)
    '(6 6) (p/make :white :pawn 6 6)
    '(7 6) (p/make :white :pawn 7 6)

    ;; White pieces
    '(0 7) (p/make :white :rook 0 7)
    '(1 7) (p/make :white :knight 1 7)
    '(2 7) (p/make :white :bishop 2 7)
    '(3 7) (p/make :white :queen 3 7)
    '(4 7) (p/make :white :king 4 7)
    '(5 7) (p/make :white :bishop 5 7)
    '(6 7) (p/make :white :knight 6 7)
    '(7 7) (p/make :white :rook 7 7)))

(defn update
  [chessboard old-pos new-pos]
  (let [chessman (get chessboard old-pos)]
    (if (m/allowed? chessboard old-pos new-pos)
      (let [new-chessman (assoc chessman :has-moved true :curr-pos new-pos)]
        (assoc chessboard old-pos nil new-pos new-chessman))
      chessboard)))

(defn count-for
  "Find the number of pieces a color has over it's opponent"
  [f color chessboard]
  (- (f color              chessboard)
     (f (p/opposite color) chessboard)))


(defn kings-count-for
  "Find the number of kings a color has over it's opponent"
  [color chessboard]
  (count-for p/kings-of color chessboard))

(defn queens-count-for
  "Find the number of queens a color has over it's opponent"
  [color chessboard]
  (count-for p/queens-of color chessboard))

(defn pawns-count-for
  "Find the number of pawns a color has over it's opponent"
  [color chessboard]
  (count-for p/pawns-of color chessboard))

(defn knights-count-for
  "Find the number of knights a color has over it's opponent"
  [color chessboard]
  (count-for p/knights-of color chessboard))

(defn bishops-count-for
  "Find the number of bishops a color has over it's opponent"
  [color chessboard]
  (count-for p/bishops-of color chessboard))

(defn rooks-count-for
  "Find the number of rooks a color has over it's opponent"
  [color chessboard]
  (count-for p/rooks-of color chessboard))

(defn mobility-for
  [color chessboard]
  (m/total-legal-moves color chessboard))

(defn score
  "This is the Claude Shannon formula featured on: https://chessprogramming.wikispaces.com/Evaluation"
  [color chessboard]
  (let [k (kings-count-for color chessboard)
        q (queens-count-for color chessboard)
        r (rooks-count-for color chessboard)
        n (knights-count-for color chessboard)
        b (bishops-count-for color chessboard)
        p (pawns-count-for color chessboard)
        m (mobility-for color chessboard)
        ]
    ;; (println "m is " m)
    (+
     (* 200.0   k)
     (*   9.0   q)
     (*   5.0   r)
     (*   3.0   n)
     (*   3.0   b)
     (*   1.0   p)
     (*   0.1   m)

     )))
