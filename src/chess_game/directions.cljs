(ns ^:figwheel-always chess-game.directions)

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
      (println "*** move-down x, y: " curr-x ", " y)
      [curr-x y])
    ))

(defn move-up
  [curr-x curr-y]

  (for [y (reverse (range 0 curr-y))]
    (do
      (println "*** move-up x, y: " curr-x ", " y)
      [curr-x y])
    ))


(defn moves-for-knight
  [x y]
  [
   [(- x 1) (+ y 2)]
   [(+ x 1) (+ y 2)]

   [(- x 1) (- y 2)]
   [(+ x 1) (- y 2)]

   [(- x 2) (+ y 1)]
   [(+ x 2) (+ y 1)]

   [(- x 2) (- y 1)]
   [(+ x 2) (- y 1)]
   ])



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
