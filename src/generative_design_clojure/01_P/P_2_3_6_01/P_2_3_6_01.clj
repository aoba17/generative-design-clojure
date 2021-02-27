(ns generative-design-clojure.01-P.P-2-3-6-01.P-2-3-6-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def data-location "src/generative_design_clojure/01_P/P_2_3_6_01/data/")
(def tile-size 30)

(defn vec2d [x y init]
  (vec (repeat y
               (vec (repeat x (init x y))))))

(defn init-tiles [_ _]
  0)

(defn setup []
  (let [tile-width  (+ (q/round (/ (q/width) tile-size)) 2)
        tile-height (+ (q/round (/ (q/height) tile-size)) 2)
        font        (q/create-font "SansSerif" 8)]
    (q/text-font font 8)
    (q/text-align :center :center)
    {:grid-resolution-x tile-width
     :grid-resolution-y tile-height
     :draw-grid?        true
     :debug-mode        false
     :tiles             (vec2d tile-width tile-height init-tiles)
     :modules           (for [i (range 16)]
                          (q/load-shape
                            (str data-location
                                 (if (< i 10) (str "0" i) i)
                                 ".svg")))}))

(defn draw-grid [tiles
                 grid-resolution-x
                 grid-resolution-y
                 debug-mode]
  (q/rect-mode :center)
  (q/stroke-weight 0.15)
  (dotimes [grid-x grid-resolution-x]
    (dotimes [grid-y grid-resolution-y]
      (if (and debug-mode
               (= (get-in tiles [grid-y grid-x]) 1))
        (q/fill 220)
        (q/fill 255))
      (q/rect
        (- (* tile-size grid-x)
           (/ tile-size 2))
        (- (* tile-size grid-y)
           (/ tile-size 2))
        tile-size
        tile-size))))

(defn draw-modules [tiles
                    grid-resolution-x
                    grid-resolution-y
                    modules
                    debug-mode]
  (q/shape-mode :center)
  (doseq [grid-y (range 1 (dec grid-resolution-y))]
    (doseq [grid-x (range 1 (dec grid-resolution-x))]
      (when (= (get-in tiles [grid-y grid-x]) 1)
        (let [east           (get-in tiles [grid-y (inc grid-x)])
              south          (get-in tiles [(inc grid-y) grid-x])
              west           (get-in tiles [grid-y (dec grid-x)])
              north          (get-in tiles [(dec grid-y) grid-x])
              binary-result  (str north west south east)
              decimal-result (Integer/parseInt binary-result 2)
              pos-x          (- (* tile-size grid-x)
                                (/ tile-size 2))
              pos-y          (- (* tile-size grid-y)
                                (/ tile-size 2))]
          (q/shape (nth modules decimal-result)
                   pos-x pos-y tile-size tile-size)
          (when debug-mode
            (q/fill 150)
            (q/text (str decimal-result "\n" binary-result)
                    pos-x pos-y)))))))

(defn draw [{:keys [grid-resolution-x
                    grid-resolution-y
                    draw-grid?
                    debug-mode
                    tiles
                    modules]}]
  (q/background 255)
  (when draw-grid?
    (draw-grid
      tiles
      grid-resolution-x
      grid-resolution-y
      debug-mode))
  (draw-modules
    tiles
    grid-resolution-x
    grid-resolution-y
    modules
    debug-mode))

(defn key-released [{:keys [grid-resolution-x
                            grid-resolution-y]
                     :as   state}
                    {:keys [key key-code]}]
  (if (or (= key-code 8)
          (= key-code 127))
    (assoc state :tiles (vec2d grid-resolution-x grid-resolution-y init-tiles))
    (case key
      :g (update state :draw-grid? not)
      :d (update state :debug-mode not)
      state)))

(defn mouse-pressed [{:keys [grid-resolution-x
                             grid-resolution-y]
                      :as   state}
                     {:keys [x y button]}]
  (let [grid-x (q/constrain
                 (inc (q/floor (/ x tile-size)))
                 1
                 (- grid-resolution-x 2))
        grid-y (q/constrain
                 (inc (q/floor (/ y tile-size)))
                 1
                 (- grid-resolution-y 2))]
    (case button
      :left  (assoc-in state [:tiles grid-y grid-x]  1)
      :right (assoc-in state [:tiles grid-y grid-x]  0)
      state)))

(q/defsketch P-2-3-6-01
  :middleware [m/fun-mode]
  :size [(q/screen-width) (q/screen-height)]
  :setup setup
  :draw draw
  :mouse-pressed mouse-pressed
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
