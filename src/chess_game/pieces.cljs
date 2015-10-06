(ns ^:figwheel-always chess-game.pieces)

(defn make
  [color type start-x start-y]
  "Creates a hash table for storing a chessman"
  {:color color                                             ; Should only ever be :black or :white
   :type type                                               ; Should only ever be one of [:pawn, :king, :bishop,
   :has-moved false                                         ;                             :queen, :rook, :knight]
   :captured false
   :curr-pos [start-x start-y]
   :start-x start-x
   :start-y start-y})

(defn white?
  [piece]
  (= (:color piece) :white))

(defn black?
  [piece]
  (= (:color piece) :black))

(defn color-matches?
  [color piece]
  (= (:color piece) color))

(defn type-matches?
  [type piece]
  (= (:type piece) type))

(defn king?
  [piece]
  (type-matches? :king piece))

(defn pawn?
  [piece]
  (type-matches? :pawn piece))

(defn knight?
  [piece]
  (type-matches? :knight piece))

(defn bishop?
  [piece]
  (type-matches? :bishop piece))

(defn queen?
  [piece]
  (type-matches? :queen piece))

(defn rook?
  [piece]
  (type-matches? :rook piece))


(defn pieces-of
  [f color chessboard]
  (count (filter #(and (f %) (color-matches? color %))
                  (vals chessboard))
          ))

(defn kings-of
  "How many kings on the board of a given color?"
  [color chessboard]
  (pieces-of king? color chessboard))

(defn pawns-of
  "How many pawns on the board of a given color?"
  [color chessboard]
  (pieces-of pawn? color chessboard))

(defn knights-of
  "How many knights on the board of a given color?"
  [color chessboard]
  (pieces-of knight? color chessboard))

(defn bishops-of
  "How many knights on the board of a given color?"
  [color chessboard]
  (pieces-of bishop? color chessboard))

(defn queens-of
  "How many queens on the board of a given color?"
  [color chessboard]
  (pieces-of queen? color chessboard))

(defn rooks-of
  "How many rooks on the board of a given color?"
  [color chessboard]
  (pieces-of queen? color chessboard))


(defn opposite
  [color]
  (case color
    :white :black
    :black :white
    nil    nil))
