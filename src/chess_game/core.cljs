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
           :legal-next-moves []
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
        {:keys [selected-tile legal-next-moves chessboard]} @env
        selected              (map #(.floor js/Math (/ % config/tile-size))  [x y])
        clear-selected-tile   (m/allowed? chessboard selected-tile selected)
        legal-next-moves      (m/legal-moves-for selected
                                                 (get chessboard selected) chessboard)
        ]

    (println "Next legal moves => " legal-next-moves)

    (swap! env assoc
           :selected-tile (if clear-selected-tile
                            nil
                            (when-not (= selected-tile selected) selected))
           :legal-next-moves legal-next-moves
           :chessboard (board/update-board chessboard
                                           selected-tile
                                           selected))))

(defn mouse-event-full
  []
  {:x (q/mouse-x)
   :y (q/mouse-y)
   :button (q/mouse-button)})

(def counter (atom 1))

(defn on-mouse-pressed
  []
  "Handle mouse press"
  (mark-selected-tile! (mouse-event-full)))


;; define your app data so that it doesn't get over-written on reload

(fw/start {
           ;; configure a websocket url if you are using your own server
           ;; :websocket-url "ws://localhost:3449/figwheel-ws"

           ;; optional callback
           :on-jsload (fn [] (print "reloaded"))

           ;; The heads up display is enabled by default
           ;; to disable it:
           ;; :heads-up-display false

           ;; when the compiler emits warnings figwheel
           ;; blocks the loading of files.
           ;; To disable this behavior:
           ;; :load-warninged-code true

           ;; if figwheel is watching more than one build
           ;; it can be helpful to specify a build id for
           ;; the client to focus on
           ;; :build-id "example"
           })



(defn setup []
  nil)
