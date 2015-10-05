(ns ^:figwheel-always chess-game.moves
    (:require [chess-game.directions :as d]))

(defn moves-for-f
  [f curr-x curr-y color chessboard]
  (println "moves-for-f where x, y: " curr-x "," curr-y)
  (for [pos (f curr-x curr-y)
        :while (nil? (get chessboard pos))]
    pos))

(defn attack-for-f
  [f curr-x curr-y curr-color chessboard]
  (println "attacks-for-f where x, y: " curr-x "," curr-y)
  (let [v (filter #(some? (get chessboard %)) (f curr-x curr-y))]
    (if (or (empty? v) (= (:color (get chessboard (first v))) curr-color))
      []
      (vector (first v)))))

(defn attacks-for-f
  [f curr-x curr-y curr-color chessboard]
  (println "attacks-for-f where x, y: " curr-x "," curr-y)
  (let [v (filter #(some? (get chessboard %)) (f curr-x curr-y))]
    (if (or (empty? v) (= (:color (get chessboard (first v))) curr-color))
      []
      v)))

(defn legal-moves-for-rook
  [x y color chessboard]
  (do
    "Attempt moves in all the cardinal directions."
    (println "legal-moves-for-rook x, y: " x ", " y)
     (concat
      (moves-for-f d/move-right x y color chessboard)
      (moves-for-f d/move-left x y color chessboard)
      (moves-for-f d/move-down x y color chessboard)
      (moves-for-f d/move-up x y color chessboard)
      )))

(defn attack-moves-for-rook
  [x y color chessboard]
  (do
    "Attempt attacks in all the cardinal directions."
    (println "attack-moves-for-rook x, y: " x ", " y)
     (concat
      (attack-for-f d/move-right x y color chessboard)
      (attack-for-f d/move-left x y color chessboard)
      (attack-for-f d/move-down x y color chessboard)
      (attack-for-f d/move-up x y color chessboard)
      )))

(defn legal-moves-for-knight
  [x y color chessboard]
  "Attempt jumps in adjacent corners"
  (moves-for-f d/moves-for-knight x y color chessboard))

(defn attack-moves-for-knight
  [x y color chessboard]
  (attacks-for-f d/moves-for-knight x y color chessboard))

(defn legal-moves-for-bishop
  [x y color chessboard]
  "Attempt moves in all the diagonal directions"
  (do
    (println "legal-moves-for-bishop x, y: " x ", " y)
    (concat
     (moves-for-f d/diagonal-upper-right x y color chessboard)
     (moves-for-f d/diagonal-lower-left x y color chessboard)
     (moves-for-f d/diagonal-lower-right x y color chessboard)
     (moves-for-f d/diagonal-upper-left x y color chessboard)
     )))

(defn attack-moves-for-bishop
  [x y color chessboard]
    (concat
     (attack-for-f d/diagonal-upper-right x y color chessboard)
     (attack-for-f d/diagonal-lower-left x y color chessboard)
     (attack-for-f d/diagonal-lower-right x y color chessboard)
     (attack-for-f d/diagonal-upper-left x y color chessboard)
     ))


(defn legal-moves-for-queen
  [x y color chessboard]
  "Attempt moves in all the diagonal directions and all the cardinal directions"
  (concat
   (moves-for-f d/move-right x y color chessboard)
   (moves-for-f d/move-left x y color chessboard)
   (moves-for-f d/move-down x y color chessboard)
   (moves-for-f d/move-up x y color chessboard)
   (moves-for-f d/diagonal-upper-right x y color chessboard)
   (moves-for-f d/diagonal-lower-left x y color chessboard)
   (moves-for-f d/diagonal-lower-right x y color chessboard)
   (moves-for-f d/diagonal-upper-left x y color chessboard)
   ))

(defn attack-moves-for-queen
  [x y color chessboard]
  (concat
   (attack-for-f d/move-right x y color chessboard)
   (attack-for-f d/move-left x y color chessboard)
   (attack-for-f d/move-down x y color chessboard)
   (attack-for-f d/move-up x y color chessboard)
   (attack-for-f d/diagonal-upper-right x y color chessboard)
   (attack-for-f d/diagonal-lower-left x y color chessboard)
   (attack-for-f d/diagonal-lower-right x y color chessboard)
   (attack-for-f d/diagonal-upper-left x y color chessboard)))

(defn legal-moves-for-king
  [x y color chessboard]
  "Attempt moves in all the diagonal directions and the cardinal directions by only one space"
  (concat
   (moves-for-f d/diagonal-upper-right-by-1 x y color chessboard)
   (moves-for-f d/diagonal-lower-left-by-1 x y color chessboard)
   (moves-for-f d/diagonal-lower-right-by-1 x y color chessboard)
   (moves-for-f d/diagonal-upper-left-by-1 x y color chessboard)
   (moves-for-f d/move-right-by-1 x y color chessboard)
   (moves-for-f d/move-left-by-1 x y color chessboard)
   (moves-for-f d/move-down-by-1 x y color chessboard)
   (moves-for-f d/move-up-by-1 x y color chessboard)
   ))


(defn attack-moves-for-king
  [x y color chessboard]
  (concat
   (attack-for-f d/diagonal-upper-right-by-1 x y color chessboard)
   (attack-for-f d/diagonal-lower-left-by-1 x y color chessboard)
   (attack-for-f d/diagonal-lower-right-by-1 x y color chessboard)
   (attack-for-f d/diagonal-upper-left-by-1 x y color chessboard)
   (attack-for-f d/move-right-by-1 x y color chessboard)
   (attack-for-f d/move-left-by-1 x y color chessboard)
   (attack-for-f d/move-down-by-1 x y color chessboard)
   (attack-for-f d/move-up-by-1 x y color chessboard)
   ))


(defn legal-moves-for-pawn
  [x y color chessboard has-moved]
  "Attempt forward by 2 on first move.
   Attempt forward by one after the piece moved.
   Attempt diagonal forward by one space when enemy present"
  (if (= color :black)
    (do
      (if has-moved
        (moves-for-f d/move-down-by-1 x y color chessboard)
        (moves-for-f d/move-down-by-2 x y color chessboard)
        ))
    (do
      (if has-moved
        (moves-for-f d/move-up-by-1 x y color chessboard)
        (moves-for-f d/move-up-by-2 x y color chessboard))
        ))
    )


(defn attack-moves-for-pawn
  [x y color chessboard has-moved]
  (if (= color :black)
    (concat
     (attack-for-f d/diagonal-lower-right-by-1 x y color chessboard)
     (attack-for-f d/diagonal-lower-left-by-1 x y color chessboard))
    (concat
     (attack-for-f d/diagonal-upper-right-by-1 x y color chessboard)
     (attack-for-f d/diagonal-upper-left-by-1 x y color chessboard))))


(defn attack-moves-for
  [curr-pos {:keys [color type has-moved]} chessboard]
  (let [x (first curr-pos)
        y (second curr-pos)]
    (case type
      :pawn   (attack-moves-for-pawn x y color chessboard has-moved)
      :knight (attack-moves-for-knight x y color chessboard)
      :queen  (attack-moves-for-queen x y color chessboard)
      :king   (attack-moves-for-king x y color chessboard)
      :bishop (attack-moves-for-bishop x y color chessboard)
      :rook   (do
                (println "Got type :rook")
                (attack-moves-for-rook x y color chessboard))
      nil     (do
                (println "Got type nil")
                [])
    )))

(defn legal-moves-for
  [curr-pos {:keys [color type has-moved]} chessboard]
  "The list of all legal moves for the selected chessman"

  (print "Finding legal moves for type" type)
  (let [x (first curr-pos)
        y (second curr-pos)]
    (println "Feeding x y to all legal moves: " x ", " y)
    (println "Type is: " type)
    (case type
      :pawn   (legal-moves-for-pawn x y color chessboard has-moved)
      :knight (legal-moves-for-knight x y color chessboard)
      :queen  (legal-moves-for-queen x y color chessboard)
      :king   (legal-moves-for-king x y color chessboard)
      :bishop (legal-moves-for-bishop x y color chessboard)
      :rook   (do
                (println "Got type :rook")
                (legal-moves-for-rook x y color chessboard))
      nil     (do
                (println "Got type nil")
                [])
      )))

(defn allowed?
  [chessboard old-pos new-pos]
  "Returns true when move allowed"
  ;; TODO: perform reall check
  (let [old-chessman (get chessboard old-pos)
        new-chessman (get chessboard new-pos)]
    (boolean (and old-chessman old-pos new-pos
                  (or (nil? new-chessman)
                      (not= (:color old-chessman)
                            (:color new-chessman)))))))
