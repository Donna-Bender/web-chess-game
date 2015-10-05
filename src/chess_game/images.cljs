(ns chess-game.images
  (:require [quil.core :as q]
            [clj-di.core :refer [get-dep]]
            [chess-game.config :as config]))

;; TODO: Consider exporting this to a more general framework
(defn load-image!
  [file-name]
  "Load image for chessmen"
  (let [image (q/load-image file-name)]
    (q/resize image config/tile-size config/tile-size)
    image))

(defn get-images!
  []
  "Set up the chessmen images"
  {:white {:pawn (load-image!   (config/image-for "white-pawn"))
           :king (load-image!   (config/image-for "white-king"))
           :bishop (load-image! (config/image-for "white-bishop"))
           :knight (load-image! (config/image-for "white-knight"))
           :rook (load-image!   (config/image-for "white-rook"))
           :queen (load-image!  (config/image-for "white-queen"))}
   :black {:pawn (load-image!   (config/image-for "black-pawn"))
           :king (load-image!   (config/image-for "black-king"))
           :bishop (load-image! (config/image-for "black-bishop"))
           :knight (load-image! (config/image-for "black-knight"))
           :rook (load-image!   (config/image-for "black-rook"))
           :queen (load-image!  (config/image-for "black-queen"))}})

(defn draw-image-on-grid!
  [image [x y]]
  "Convenience method for drawing on a grid of size `tile-size`."
  (q/image image (* x config/tile-size) (* y config/tile-size)))

(defn get-image-for-chessman
  [{:keys [color type]}]
  "Returns image for chessman color and type"
  (get-in (get-dep :images) [color type]))

(defn draw-chessman!
  [chessman pos]
  (draw-image-on-grid! (get-image-for-chessman chessman)
                       pos))
