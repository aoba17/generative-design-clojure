(ns generative-design-clojure.01-P.P-4-3-1-02.P-4-3-1-02
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]
            [clojure.java.io :as io]))

(def data-location "src/generative_design_clojure/01_P/P_4_3_1_02/data/")


(defn setup []
  {:draw-mode 1
   :img       (q/load-image (str data-location "pic.png"))
   :shapes    (->> data-location
                   io/file
                   file-seq
                   (filter #(.isFile %))
                   (map #(.getAbsolutePath %))
                   (filter #(re-find #"svg$" %))
                   sort
                   (map q/load-shape))})

(defn draw [{:keys [draw-mode img shapes]}]
  (q/background 255)
  (dotimes [grid-x (.-width img)]
    (dotimes [grid-y (.-height img)]
      (let [tile-width        (/ (q/width) (.-width img))
            tile-height       (/ (q/height) (.-height img))
            pos-x             (* tile-width grid-x)
            pos-y             (* tile-height grid-y)
            c                 (q/get-pixel img grid-x grid-y)
            greyscale         (q/round (+ (* (q/red c) 0.222)
                                          (* (q/green c) 0.707)
                                          (* (q/blue c) 0.071)))
            gradient-to-index (q/round
                                (q/map-range greyscale
                                             0 255
                                             0 (dec (count shapes))))]
        (q/shape (nth shapes gradient-to-index)
                 pos-x pos-y
                 tile-width tile-height)))))

(defn key-released [state event]
  (case (:key event)
    :s (do (util/save-frame)
           state)
    state))

(q/defsketch P-4-3-1-01
  :middleware [m/fun-mode]
  :size [600 900]
  :setup setup
  :draw draw
  :key-released key-released
  :features [:resizable]
  :settings q/smooth)
