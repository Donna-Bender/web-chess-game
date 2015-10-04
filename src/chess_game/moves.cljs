(ns chess-game.moves)

;; Legal moves


(defn diagonal-lower-left
  [curr-x curr-y]

  (for [x (reverse (range 0 curr-x)) ;; Go let
        y (range (+ curr-y 1) 8) ;; Go down
        :when (== (+ x y) (+ curr-x curr-y))
        ]
    (do
      (println "*** diagonal-lower-left x, y: " x ", " y)
      [x y])
    ))

(defn diagonal-lower-right
  [curr-x curr-y]

  (for [x (range (+ curr-x 1) 8)
        y (range (+ curr-y 1) 8)
        :when (== (- x y) (- curr-x curr-y))
        ]
    (do
      (println "*** diagonal-lower-right x, y: " x ", " y)
      [x y])
    ))

(defn diagonal-upper-right
  [curr-x curr-y]

  (for [x (range (+ curr-x 1) 8) ;; Go right
        y (reverse (range 0 curr-y)) ;; Go up
        :when (== (+ x y) (+ curr-x curr-y))
        ]
    (do
      (println "*** diagonal-upper-right x, y: " x ", " y)
      [x y])
    ))

(defn diagonal-upper-left
  [curr-x curr-y]

  (for [x (reverse (range 0 curr-x)) ;; Go left
        y (reverse (range 0 curr-y)) ;; Go up
        :when (== (- x y) (- curr-x curr-y))
        ]
    (do
      (println "*** diagonal-upper-left x, y: " x ", " y)
      [x y])
    ))


(defn move-right
  [curr-x curr-y]

  (for [x (range (+ curr-x 1) 8)]
    (do
      (println "*** move-right x, y: " x ", " curr-y)
      [x curr-y])
    ))

(defn move-left
  [curr-x curr-y]

  (for [x (reverse (range 0 curr-x))]
    (do
      (println "*** move-left x, y: " x ", " curr-y)
      [x curr-y])
    ))

(defn move-down
  [curr-x curr-y]

  (for [y (range (+ curr-y 1) 8)]
    (do
      (println "*** move-down x, y: " x ", " curr-y)
      [curr-x y])
    ))

(defn move-up
  [curr-x curr-y]

  (for [y (reverse (range 0 curr-y))]
    (do
      (println "*** move-up x, y: " x ", " curr-y)
      [curr-x y])
    ))


(defn moves-for-f
  [f curr-x curr-y color chessboard]
  (println "moves-for-f where x, y: " curr-x "," curr-y)
  (for [pos (f curr-x curr-y)
        :while (nil? (get chessboard pos))]
    pos))


(defn moves-by-1
  [f x y]
  (take 1 (f x y)))

(defn moves-by-2
  [f x y]
  (take 2 (f x y)))

(defn move-right-by-1
  [x y]
  (moves-by-1 move-right x y))

(defn move-left-by-1
  [x y]
  (moves-by-1 move-left x y))

(defn move-up-by-1
  [x y]
  (moves-by-1 move-up x y))

(defn move-down-by-1
  [x y]
  (moves-by-1 move-down x y))

(defn move-down-by-2
  [x y]
  (moves-by-2 move-down x y))

(defn move-up-by-2
  [x y]
  (moves-by-2 move-up x y))

(defn diagonal-upper-left-by-1
  [x y]
  (moves-by-1 diagonal-upper-left x y))

(defn diagonal-upper-right-by-1
  [x y]
  (moves-by-1 diagonal-upper-right x y))

(defn diagonal-lower-left-by-1
  [x y]
  (moves-by-1 diagonal-lower-left x y))

(defn diagonal-lower-right-by-1
  [x y]
  (moves-by-1 diagonal-lower-right x y))

(defn legal-moves-for-rook
  [x y color chessboard]
  (do
    "Attempt moves in all the cardinal directions."
    (println "legal-moves-for-rook x, y: " x ", " y)
     (concat
      (moves-for-f move-right x y color chessboard)
      (moves-for-f move-left x y color chessboard)
      (moves-for-f move-down x y color chessboard)
      (moves-for-f move-up x y color chessboard)
      )))

(defn legal-moves-for-knight
  [x y color chessboard]
  "Attempt jumps in adjacent corners"
  [
   [(- x 1) (+ y 2)]
   [(+ x 1) (+ y 2)]

   [(- x 1) (- y 2)]
   [(+ x 1) (- y 2)]
   ])

(defn legal-moves-for-bishop
  [x y color chessboard]
  "Attempt moves in all the diagonal directions"
  (do
    (println "legal-moves-for-bishop x, y: " x ", " y)
    (concat
     (moves-for-f diagonal-upper-right x y color chessboard)
     (moves-for-f diagonal-lower-left x y color chessboard)
     (moves-for-f diagonal-lower-right x y color chessboard)
     (moves-for-f diagonal-upper-left x y color chessboard)
     )))

(defn legal-moves-for-queen
  [x y color chessboard]
  "Attempt moves in all the diagonal directions and all the cardinal directions"
  (concat
   (moves-for-f move-right x y color chessboard)
   (moves-for-f move-left x y color chessboard)
   (moves-for-f move-down x y color chessboard)
   (moves-for-f move-up x y color chessboard)
   (moves-for-f diagonal-upper-right x y color chessboard)
   (moves-for-f diagonal-lower-left x y color chessboard)
   (moves-for-f diagonal-lower-right x y color chessboard)
   (moves-for-f diagonal-upper-left x y color chessboard)
   ))


(defn legal-moves-for-king
  [x y color chessboard]
  "Attempt moves in all the diagonal directions and the cardinal directions by only one space"
  (concat
   (moves-for-f diagonal-upper-right-by-1 x y color chessboard)
   (moves-for-f diagonal-lower-left-by-1 x y color chessboard)
   (moves-for-f diagonal-lower-right-by-1 x y color chessboard)
   (moves-for-f diagonal-upper-left-by-1 x y color chessboard)
   (moves-for-f move-right-by-1 x y color chessboard)
   (moves-for-f move-left-by-1 x y color chessboard)
   (moves-for-f move-down-by-1 x y color chessboard)
   (moves-for-f move-up-by-1 x y color chessboard)
   ))

(defn legal-moves-for-pawn
  [x y color chessboard has-moved]
  "Attempt forward by 2 on first move.
   Attempt forward by one after the piece moved.
   Attempt diagonal forward by one space when enemy present"
  (if (= color :black)
    (do
      (println "Color is :black")
      (moves-for-f move-down-by-2 x y color chessboard))
    (do
      (println "Color is :white")
      (moves-for-f move-up-by-2 x y color chessboard))
    ))


(defn legal-move? [start-pos dest-pos chessman chessboard]
  "Decide of the move of chessman to dest-pos"
  nil)

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
