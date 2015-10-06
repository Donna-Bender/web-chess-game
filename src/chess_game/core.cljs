(ns ^:figwheel-always chess-game.core
  (:require [quil.core :as q]
            [quil.middleware]
            [jayq.core :as jq]
            [clj-di.core :refer [get-dep register!]]
            [chess-game.moves :as m]
            [chess-game.config :as config]
            [chess-game.images :refer [get-images!]]
            [chess-game.board :as b]
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

(defn legal-moves
  [curr-tile selected selected-piece chessboard]
  (if (= curr-tile selected)
    []
    (m/legal-moves-for selected selected-piece chessboard)))


(defn print-scores
  [word board]
  (println word "move white score = " (b/score :white board))
  (println word "move black score = " (b/score :black board)))


(defn try-to-mark-tile!
  "Mark the tile selected by the click event"
  [{:keys [x y]}]
  (let [env (get-dep :env)
        {:keys [curr-tile chessboard]} @env
        selected              (selected-from x y)
        selected-piece        (get chessboard selected)
        piece-moved           (m/allowed? chessboard curr-tile selected)
        attack-moves          (m/attack-moves-for selected selected-piece chessboard)]

    (let [new-chessboard (b/update chessboard curr-tile selected)
          legal-moves    (legal-moves curr-tile selected selected-piece chessboard)
          new-curr-tile  (if piece-moved
                           nil
                           (when-not (= curr-tile selected) selected))]
      (if piece-moved
        (print-scores "After" new-chessboard))

      (swap! env assoc
             :curr-tile     new-curr-tile
             :legal-moves   legal-moves
             :attack-moves  (if (= curr-tile selected)
                              []
                              attack-moves)
             :chessboard    new-chessboard)
    )))

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
