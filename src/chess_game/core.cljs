(ns ^:figwheel-always chess-game.core
  (:require [quil.core :as q]
            [quil.middleware]
            [jayq.core :as jq]
            [clj-di.core :refer [get-dep register!]]
            [chess-game.moves :as m]
            [chess-game.config :as config]
            [chess-game.images :refer [get-images!]]
            [chess-game.board :as b]
            [chess-game.score :as s]
            [chess-game.pieces :as p]
            [chess-game.drawing :as drawing]))

(enable-console-print!)

(defn setup! []
  "Initialize everything"
  ;; :chessboard
  ;; A hash-map representing the current chessboard
  ;;
  ;; :curr-tile
  ;; The currently selected tile, after being clicked
  (let [env (atom {})]
    (swap! env assoc
           :chessboard (b/make-standard-board)
           :legal-moves []
           :attack-moves []
           :curr-tile nil)
    (register! :env env
               :images (get-images!))))

(defn draw!
  "Draw the sketch"
  []
  (drawing/draw-checkered-board)
  (drawing/draw-chessmens!))

(defn selected-from
  [x y]
  (map #(.floor js/Math (/ % config/tile-size)) [x y]))



(defn print-scores
  [word board]
  (println word "move white score = " (b/score-board :white board))
  (println word "move black score = " (b/score-board :black board)))

(defn in?
  [seq elem]
  (some #(= elem %) seq))

(defn not-in?
  [seq elem]
  (not-any? #(= elem %) seq))

(defn try-to-mark-tile!
  "Mark the tile selected by the click event"
  [{:keys [x y]}]
  (let [env (get-dep :env)
        {:keys [last-piece attack-moves legal-moves curr-tile chessboard]} @env
        selected              (selected-from x y)
        selected-piece        (if (in? attack-moves selected)
                                nil
                                (get chessboard selected))

        white-selected        (cond
                                (some? selected-piece) (p/white? selected-piece)
                                (some? last-piece) (p/white? last-piece))]


    (if white-selected

      (let [curr-legal-moves      (cond
                                    (= curr-tile selected)      []
                                    (in? attack-moves selected) []
                                    :else (m/legal-moves-for selected selected-piece chessboard))
            curr-attack-moves     (m/attack-moves-for
                                   selected selected-piece chessboard)

            piece-will-move       (or (in? legal-moves selected)
                                      (in? attack-moves selected))
            new-chessboard        (if piece-will-move
                                    (b/update-board chessboard curr-tile selected)
                                    chessboard)
            new-curr-tile         (if piece-will-move
                                    nil
                                    (when-not (= curr-tile selected) selected))]


        (swap! env assoc
               :curr-tile     new-curr-tile
               :last-piece    selected-piece
               :legal-moves   curr-legal-moves
               :attack-moves  (if (= curr-tile selected)
                                []
                                curr-attack-moves)
               :chessboard    new-chessboard)


        (if (some? last-piece)
          (let [color     (p/opposite (:color last-piece))
                max-score (s/max-score-next-legal-moves color new-chessboard)]
            (do
              (println "Scoring for color" color)
              (println "Max scoring move is" max-score)

              (if (some? max-score)
                (swap! env assoc
                        :chessboard (b/update-board new-chessboard
                                                    (:old-pos max-score)
                                                    (:next max-score))))
              )))
        ))))

(defn mouse-event-full
  []
  {:x (q/mouse-x)
   :y (q/mouse-y)
   :button (q/mouse-button)})

(defn on-mouse-pressed
  []
  "Handle mouse press"
  (try-to-mark-tile! (mouse-event-full)))

(jq/document-ready
  (try (q/sketch :title "Chess board"
                 :size config/board-size
                 :host "canvas"
                 :setup setup!
                 :mouse-pressed on-mouse-pressed
                 :draw draw!)
       (catch :default e (.error js/console e))))
