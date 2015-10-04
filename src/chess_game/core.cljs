(ns ^:figwheel-always chess-game.core
  (:require [quil.core :as q]
            [quil.middleware]
            [jayq.core :as jq]
            [clj-di.core :refer [get-dep register!]]
            [chess-game.moves :as m]
            [chess-game.config :as config]
            [chess-game.images :refer [get-images!]]
            [chess-game.board :as board]
            [chess-game.drawing :as drawing]))

(enable-console-print!)

(defn setup! []
  "Initialize everything"
  ;; :chessboard
  ;; A hash-map representing the current chessboard
  ;;
  ;; :selected-tile
  ;; The currently selected tile, after being clicked
  (let [env (atom {})]
    (swap! env assoc
           :chessboard (board/make-standard-board)
           :legal-moves []
           :attack-moves []
           :selected-tile nil)
    (register! :env env
               :images (get-images!))))

(defn draw!
  []
  "Draw the sketch"
  (drawing/draw-checkered-board)
  (drawing/draw-chessmens!))

(defn mark-selected-tile!
  [{:keys [x y]}]
  "Mark the tile selected by the click event"
  (let [env (get-dep :env)
        {:keys [selected-tile chessboard]} @env
        selected              (map #(.floor js/Math (/ % config/tile-size))  [x y])
        clear-selected-tile   (m/allowed? chessboard selected-tile selected)
        legal-moves           (m/legal-moves-for
                               selected
                               (get chessboard selected) chessboard)
        attack-moves          (m/attack-moves-for
                               selected
                               (get chessboard selected) chessboard)
        ]

    (println "Next legal moves => " legal-moves)
    (println "Next attack moves => " attack-moves)

    (swap! env assoc
           :selected-tile (if clear-selected-tile
                            nil
                            (when-not (= selected-tile selected) selected))
           :legal-moves   (if (= selected-tile selected)
                            []
                            legal-moves)
           :attack-moves  (if (= selected-tile selected)
                            []
                            attack-moves)
           :chessboard    (board/update-board chessboard
                                              selected-tile
                                              selected))))

(defn mouse-event-full
  []
  {:x (q/mouse-x)
   :y (q/mouse-y)
   :button (q/mouse-button)})

(defn on-mouse-pressed
  []
  "Handle mouse press"
  (mark-selected-tile! (mouse-event-full)))

(jq/document-ready
  (try (q/sketch :title "Chess board"
                 :size config/board-size
                 :host "canvas"
                 :setup setup!
                 :mouse-pressed on-mouse-pressed
                 :draw draw!)
       (catch :default e (.error js/console e))))
