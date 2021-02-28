(ns generative-design-clojure.01-P.P-2-3-6-02.P-2-3-6-02
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def data-location "src/generative_design_clojure/01_P/P_2_3_6_02/data/")
(def tile-size 30)

(defn vec2d [x y init]
  (vec (repeat y
               (vec (repeat x (init x y))))))

(defn init-tiles [_ _]
  0)

(defn init-tile-colors [_ _]
  (q/color (q/random 255) 0 (q/random 255)))

(defn load-modules [prefix]
  (for [i (range 16)]
    (let [shape (q/load-shape
                  (str data-location
                       prefix
                       (if (< i 10) (str "0" i) i)
                       ".svg"))]
      (.disableStyle shape)
      shape)))

(defn setup []
  (let [tile-width  (+ (q/round (/ (q/width) tile-size)) 2)
        tile-height (+ (q/round (/ (q/height) tile-size)) 2)
        font        (q/create-font "SansSerif" 8)]
    (q/color-mode :hsb 360 100 100 100)
    (q/cursor :cross)
    (q/text-font font 8)
    (q/text-align :center :center)
    {:grid-resolution-x  tile-width
     :grid-resolution-y  tile-height
     :draw-grid?         true
     :debug-mode         false
     :random-mode        false
     :tiles              (vec2d tile-width tile-height init-tiles)
     :tile-colors        (vec2d tile-width tile-height init-tile-colors)
     :modules-set        {:a (load-modules "A_")
                          :b (load-modules "B_")
                          :c (load-modules "C_")
                          :d (load-modules "D_")
                          :e (load-modules "E_")
                          :f (load-modules "F_")
                          :j (load-modules "J_")
                          :k (load-modules "K_")}
     :active-modules-set :a
     :active-tile-color  (q/color 0)}))

(defn draw-grid [tiles
                 grid-resolution-x
                 grid-resolution-y
                 debug-mode]
  (q/rect-mode :center)
  (q/stroke-weight 0.15)
  (q/stroke 0)
  (q/no-fill)
  (dotimes [grid-x grid-resolution-x]
    (dotimes [grid-y grid-resolution-y]
      (if (and debug-mode
               (= (get-in tiles [grid-y grid-x]) 1))
        (q/fill 220)
        (q/fill 360))
      (q/rect
        (- (* tile-size grid-x)
           (/ tile-size 2))
        (- (* tile-size grid-y)
           (/ tile-size 2))
        tile-size
        tile-size))))

(defn draw-modules [tiles
                    tile-colors
                    grid-resolution-x
                    grid-resolution-y
                    modules-set
                    debug-mode
                    random-mode]
  (q/shape-mode :center)
  (doseq [grid-y (range 1 (dec grid-resolution-y))]
    (doseq [grid-x (range 1 (dec grid-resolution-x))]
      (let [current-tile (get-in tiles [grid-y grid-x])]
        (when (not= current-tile 0)
          (let [north          (if (not= (get-in tiles [(dec grid-y) grid-x]) 0)
                                 1 0)
                west           (if (not= (get-in tiles [grid-y (dec grid-x)]) 0)
                                 1 0)
                south          (if (not= (get-in tiles [(inc grid-y) grid-x]) 0)
                                 1 0)
                east           (if (not= (get-in tiles [grid-y (inc grid-x)]) 0)
                                 1 0)
                binary-result  (str north west south east)
                decimal-result (Integer/parseInt binary-result 2)
                pos-x          (- (* tile-size grid-x)
                                  (/ tile-size 2))
                pos-y          (- (* tile-size grid-y)
                                  (/ tile-size 2))]
            (q/fill (get-in tile-colors [grid-y grid-x]))
            (q/no-stroke)
            (if random-mode
              (q/shape (-> modules-set vals rand-nth (nth decimal-result))
                       pos-x pos-y tile-size tile-size)
              (case current-tile
                :a (q/shape (-> modules-set :a (nth decimal-result))
                            pos-x pos-y tile-size tile-size)
                :b (q/shape (-> modules-set :b (nth decimal-result))
                            pos-x pos-y tile-size tile-size)
                :c (q/shape (-> modules-set :c (nth decimal-result))
                            pos-x pos-y tile-size tile-size)
                :d (q/shape (-> modules-set :d (nth decimal-result))
                            pos-x pos-y tile-size tile-size)
                :e (q/shape (-> modules-set :e (nth decimal-result))
                            pos-x pos-y tile-size tile-size)
                :f (q/shape (-> modules-set :f (nth decimal-result))
                            pos-x pos-y tile-size tile-size)
                :j (q/shape (-> modules-set :j (nth decimal-result))
                            pos-x pos-y tile-size tile-size)
                :k (q/shape (-> modules-set :k (nth decimal-result))
                            pos-x pos-y tile-size tile-size)))
            (when debug-mode
              (q/fill 150)
              (q/text (str current-tile "\n" decimal-result "\n" binary-result)
                      pos-x pos-y))))))))

(defn draw [{:keys [grid-resolution-x
                    grid-resolution-y
                    draw-grid?
                    debug-mode
                    tiles
                    tile-colors
                    modules-set
                    random-mode]}]
  (q/background 360)
  (when draw-grid?
    (draw-grid
      tiles
      grid-resolution-x
      grid-resolution-y
      debug-mode))
  (draw-modules
    tiles
    tile-colors
    grid-resolution-x
    grid-resolution-y
    modules-set
    debug-mode
    random-mode))

(defn key-released [{:keys [grid-resolution-x
                            grid-resolution-y]
                     :as   state}
                    {:keys [key key-code]}]
  (if (or (= key-code 8)
          (= key-code 127))
    (assoc state
           :tiles (vec2d grid-resolution-x grid-resolution-y init-tiles)
           :tile-colors (vec2d grid-resolution-x grid-resolution-y init-tile-colors))
    (case key
      :g (update state :draw-grid? not)
      :d (update state :debug-mode not)
      :r (update state :random-mode not)
      :1 (assoc state :active-modules-set :a)
      :2 (assoc state :active-modules-set :b)
      :3 (assoc state :active-modules-set :c)
      :4 (assoc state :active-modules-set :d)
      :5 (assoc state :active-modules-set :e)
      :6 (assoc state :active-modules-set :f)
      :7 (assoc state :active-modules-set :j)
      :8 (assoc state :active-modules-set :k)
      :y (assoc state :active-tile-color (q/color 0))
      :x (assoc state :active-tile-color (q/color 52 100 71))
      :c (assoc state :active-tile-color (q/color 192 100 64))
      :v (assoc state :active-tile-color (q/color 273 73 51))
      :b (assoc state :active-tile-color (q/color 323 100 77))
      state)))

(defn mouse-pressed [{:keys [grid-resolution-x
                             grid-resolution-y
                             active-modules-set
                             active-tile-color]
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
      :left  (-> state
                 (assoc-in [:tiles grid-y grid-x] active-modules-set)
                 (assoc-in [:tile-colors grid-y grid-x] active-tile-color))
      :right (assoc-in state [:tiles grid-y grid-x]  0)
      state)))

(q/defsketch P-2-3-6-02
  :middleware [m/fun-mode]
  :size [(q/screen-width) (q/screen-height)]
  :setup setup
  :draw draw
  :mouse-pressed mouse-pressed
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
