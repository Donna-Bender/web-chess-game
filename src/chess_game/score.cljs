(ns ^:figwheel-always chess-game.score
  (:require [chess-game.moves :as m]
            [chess-game.board :as b]
            ))

(defn score-next-legal-moves
  [color chessboard]
  (for [move (m/next-legal-moves color chessboard)]
    (let [{:keys [piece next]} move
          old-pos              (:curr-pos piece)
          next-board           (b/update-board chessboard old-pos next)
          score                (b/score-board color next-board)]
        {:score score :old-pos old-pos :next next})))

(defn max-score-next-legal-moves
  [color chessboard]
  (apply (partial max-key :score) (score-next-legal-moves color chessboard)))

(defn min-score-next-legal-moves
  [color chessboard]
  (apply (partial min-key :score) (score-next-legal-moves color chessboard)))
