(ns ^:figwheel-always chess-game.moves
    (:require [chess-game.directions :as d]
              [chess-game.pieces :as p]
              ))

(defn open?
  [pos chessboard]
  (nil? (get chessboard pos)))

(defn filled?
  [pos chessboard]
  (some? (get chessboard pos)))

(defn color-of
  [pos chessboard]
  (:color (get chessboard pos)))

(defn moves-for-f
  [f curr-x curr-y color chessboard]
  (for [pos (f curr-x curr-y)
        :while (open? pos chessboard)]
    pos))

(defn attack-for-f
  [f curr-x curr-y curr-color chessboard]
  (let [v (filter #(some? (get chessboard %)) (f curr-x curr-y))]
    (if (or (empty? v) (= (color-of (first v) chessboard) curr-color))
      []
      (vector (first v)))))

(defn attacks-for-f
  [f curr-x curr-y curr-color chessboard]
  (let [v (filter #(some? (get chessboard %)) (f curr-x curr-y))]
    (if (or (empty? v) (= (color-of (first v) chessboard) curr-color))
      []
      v)))

(defn legal-moves-for-rook
  "Attempt moves in all the cardinal directions."
  [x y color chessboard]
  (concat
   (moves-for-f d/move-right x y color chessboard)
   (moves-for-f d/move-left x y color chessboard)
   (moves-for-f d/move-down x y color chessboard)
   (moves-for-f d/move-up x y color chessboard)
   ))

(defn attack-moves-for-rook
  "Attempt attacks in all the cardinal directions."
  [x y color chessboard]
  (concat
   (attack-for-f d/move-right x y color chessboard)
   (attack-for-f d/move-left x y color chessboard)
   (attack-for-f d/move-down x y color chessboard)
   (attack-for-f d/move-up x y color chessboard)
   ))

(defn legal-moves-for-knight
  "Attempt jumps in adjacent corners"
  [curr-x curr-y color chessboard]
  (for [pos (d/moves-for-knight curr-x curr-y)
        :when (open? pos chessboard)]
    pos))


(defn attackable?
  [curr-color chessboard curr-pos]
  (and (filled? curr-pos chessboard)
       (not= curr-color (color-of curr-pos chessboard))))

(defn attack-moves-for-knight
  [curr-x curr-y curr-color chessboard]
  (let [
        moves     (d/moves-for-knight curr-x curr-y)
        available (filter #(some? (get chessboard %)) moves)
        attacking (filter attackable? moves)
        ]
    (if (empty? attacking)
      []
      attacking)))


(defn legal-moves-for-bishop
  "Attempt moves in all the diagonal directions"
  [x y color chessboard]
  (concat
   (moves-for-f d/diagonal-upper-right x y color chessboard)
   (moves-for-f d/diagonal-lower-left x y color chessboard)
   (moves-for-f d/diagonal-lower-right x y color chessboard)
   (moves-for-f d/diagonal-upper-left x y color chessboard)
   ))

(defn attack-moves-for-bishop
  [x y color chessboard]
    (concat
     (attack-for-f d/diagonal-upper-right x y color chessboard)
     (attack-for-f d/diagonal-lower-left x y color chessboard)
     (attack-for-f d/diagonal-lower-right x y color chessboard)
     (attack-for-f d/diagonal-upper-left x y color chessboard)
     ))


(defn legal-moves-for-queen
  "Attempt moves in all the diagonal directions and all the cardinal directions"
  [x y color chessboard]
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
  "Attempt moves in all the diagonal directions and the cardinal directions by only one space"
  [x y color chessboard]
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
  "Attempt forward by 2 on first move.
   Attempt forward by one after the piece moved.
   Attempt diagonal forward by one space when enemy present"
  [x y color chessboard has-moved]
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
      :rook   (attack-moves-for-rook x y color chessboard)
      nil     []
    )))

(defn legal-moves-by-type
  [type x y color has-moved chessboard]
  (case type
    :pawn   (legal-moves-for-pawn x y color chessboard has-moved)
    :knight (legal-moves-for-knight x y color chessboard)
    :queen  (legal-moves-for-queen x y color chessboard)
    :king   (legal-moves-for-king x y color chessboard)
    :bishop (legal-moves-for-bishop x y color chessboard)
    :rook   (legal-moves-for-rook x y color chessboard)
    nil     []
    ))

(defn legal-moves-for-piece
  [piece chessboard]
  (let [type      (:type piece)
        pos       (:curr-pos piece)
        x         (first pos)
        y         (second pos)
        color     (:color piece)
        has-moved (:has-moved piece)
        legal-moves (legal-moves-by-type type x y color has-moved chessboard)
        ]
    ;; (println "# of legal moves for " type " of color " color " are " (count legal-moves))
    legal-moves))

(defn total-legal-moves
  [curr-color chessboard]
  (let [positions  (keys chessboard)
        all-pieces (map #(get chessboard %) positions)
        curr-pieces (filter #(p/color-matches? curr-color %) all-pieces)
        counts (map #(count (legal-moves-for-piece % chessboard)) curr-pieces)
        ]
    (reduce + 0 counts)))



(defn legal-moves-for
  "The list of all legal moves for the selected chessman"
  [curr-pos {:keys [color type has-moved]} chessboard]
  (let [x (first curr-pos)
        y (second curr-pos)]
    (legal-moves-by-type type x y color has-moved chessboard)))

(defn allowed?
  "Returns true when move allowed"
  [chessboard old-pos new-pos]
  ;; TODO: perform reall check
  (let [old-chessman (get chessboard old-pos)
        new-chessman (get chessboard new-pos)]
    (boolean (and old-chessman old-pos new-pos
                  (or (nil? new-chessman)
                      (not= (:color old-chessman)
                            (:color new-chessman)))))))
