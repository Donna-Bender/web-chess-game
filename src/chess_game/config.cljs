(ns ^:figwheel-always chess-game.config
    (:require [clojure.string :as string]))


(def large-image-size 128)
(def medium-image-size 64)
(def small-image-size 32)


(def large-tile-size 120)
(def medium-tile-size 60)
(def small-image-size 30)

(def tile-size medium-tile-size)

(def current-image-size medium-image-size)

(def board-tiles-x 8)
(def board-tiles-y 8)

(def board-size
  [(* board-tiles-x tile-size)
   (* board-tiles-y tile-size)])

(defn image-for
  [name]
  (string/join ["/images/" (str current-image-size) "/" name ".png"]))
