(ns chess-game.moves)


;; Legal moves

(defn legal-moves-for-rook
  [curr-pos color chessboard]
  "Attempt moves in all the cardinal directions."
  [])

(defn legal-moves-for-knight
  [curr-pos color chessboard]
  "Attempt jumps in adjacent corners"
  (let [x (first curr-pos)
        y (second curr-pos)]
    [
     [(- x 1) (+ y 2)]
     [(+ x 1) (+ y 2)]

     [(- x 1) (- y 2)]
     [(+ x 1) (- y 2)]


     ]))

(defn legal-moves-for-bishop
  [curr-pos color chessboard]
  "Attempt moves in all the diagonal directions"
  [])

(defn legal-moves-for-queen
  [curr-pos color chessboard]
  "Attempt moves in all the diagonal directions and all the cardinal directions"
  [])

(defn legal-moves-for-king
  [curr-pos color chessboard]
  "Attempt moves in all the diagonal directions and the cardinal directions by only one space"
  [])

(defn legal-moves-for-pawn
  [curr-pos color chessboard]
  "Attempt forward by 2 on first move.
   Attempt forward by one after the piece moved.
   Attempt diagonal forward by one space when enemy present"
  [])

(defn legal-moves-for-rook
  [curr-pos color chessboard]
  "Attempt moves in all the cardinal directions."
  [])

(defn legal-move? [start-pos dest-pos chessman chessboard]
  "Decide of the move of chessman to dest-pos"
  nil)

(defn legal-moves-for
  [curr-pos {:keys [color type]} chessboard]
  "The list of all legal moves for the selected chessman"

  (print "Finding legal moves for type" type)

  (case type
    :pawn   (legal-moves-for-pawn curr-pos color chessboard)
    :knight (legal-moves-for-knight curr-pos color chessboard)
    :queen  (legal-moves-for-queen curr-pos color chessboard)
    :king   (legal-moves-for-king curr-pos color chessboard)
    :bishop (legal-moves-for-bishop curr-pos color chessboard)
    :rook   (legal-moves-for-rook curr-pos color chessboard)
    nil     []
    )
  )



(defn allowed?
  [chessboard old-pos new-pos]
  "Returns true when move allowed"
  ; TODO: perform reall check
  (let [old-chessman (get chessboard old-pos)
        new-chessman (get chessboard new-pos)]
    (boolean (and old-chessman old-pos new-pos
                  (or (nil? new-chessman)
                      (not= (:color old-chessman)
                            (:color new-chessman)))))))
