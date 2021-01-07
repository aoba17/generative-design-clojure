(ns generative-design-clojure.01-P.P-2-1-1-04.P-2-1-1-04
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def data-location "src/generative_design_clojure/01_P/P_2_1_1_04/data/")

(defn setup []
  {:tile-count    10
   :tile-width    (/ (q/width) 10)
   :tile-height   (/ (q/height) 10)
   :shape-size    50
   :shape-angle   0
   :max-dist      (q/sqrt (+ (q/sq (q/width))
                             (q/sq (q/height))))
   :shape-color   (q/color 0 130 164)
   :fill-mode     0
   :size-mode     0
   :current-shape (q/load-shape (str data-location "module_1.svg"))})

(defn draw [{:keys [tile-count
                    tile-width
                    tile-height
                    shape-size
                    shape-angle
                    max-dist
                    shape-color
                    fill-mode
                    size-mode
                    current-shape]}]
  (q/background 255)
  (loop [grid-y 0]
    (when (< grid-y tile-count)
      (loop [grid-x 0]
        (when (< grid-x tile-count)
          (let [pos-x          (+ (* tile-width grid-x) (/ tile-width 2))
                pos-y          (+ (* tile-height grid-y) (/ tile-height 2))
                angle          (+ (q/atan2 (- (q/mouse-y) pos-y)
                                           (- (q/mouse-x) pos-x))
                                  (q/radians shape-angle))
                new-shape-size (case size-mode
                                 0 shape-size
                                 1 (- (* shape-size 1.5)
                                      (q/map-range (q/dist (q/mouse-x) (q/mouse-y)
                                                           pos-x pos-y)
                                                   0 500
                                                   5 shape-size))
                                 2 (q/map-range (q/dist (q/mouse-x) (q/mouse-y)
                                                        pos-x pos-y)
                                                0 500
                                                5 shape-size))
                shape-r        (q/red shape-color)
                shape-g        (q/green shape-color)
                shape-b        (q/blue shape-color)]
            (case fill-mode
              0 (.enableStyle current-shape)
              1 (do (.disableStyle current-shape)
                    (q/fill shape-color))
              2 (do (.disableStyle current-shape)
                    (q/fill shape-r shape-g shape-b
                            (q/map-range (q/dist (q/mouse-x) (q/mouse-y)
                                                 pos-x pos-y)
                                         0 max-dist
                                         255 0)))
              3 (do (.disableStyle current-shape)
                    (q/fill shape-r shape-g shape-b
                            (q/map-range (q/dist (q/mouse-x) (q/mouse-y)
                                                 pos-x pos-y)
                                         0 max-dist
                                         0 255))))
            (q/with-translation [pos-x pos-y]
              (q/rotate angle)
              (q/shape-mode :center)
              (q/no-stroke)
              (q/shape current-shape
                       0 0
                       new-shape-size new-shape-size)))
          (recur (inc grid-x))))
      (recur (inc grid-y)))))

(defn key-released [{:keys [tile-count]
                     :as   state}
                    event]
  (case (:key event)
    :c     (update state :fill-mode #(mod (inc %) 4))
    :d     (update state :size-mode #(mod (inc %) 3))
    :g     (let [tmp-tile-count (+ tile-count 5)
                 new-tile-count (if (> tmp-tile-count 20)
                                  10
                                  tmp-tile-count)]
             (assoc state
                    :tile-count new-tile-count
                    :tile-width (/ (q/width)
                                   new-tile-count)
                    :tile-height (/ (q/height)
                                    new-tile-count)))
    :1     (assoc state :current-shape (q/load-shape (str data-location "module_1.svg")))
    :2     (assoc state :current-shape (q/load-shape (str data-location "module_2.svg")))
    :3     (assoc state :current-shape (q/load-shape (str data-location "module_3.svg")))
    :4     (assoc state :current-shape (q/load-shape (str data-location "module_4.svg")))
    :5     (assoc state :current-shape (q/load-shape (str data-location "module_5.svg")))
    :6     (assoc state :current-shape (q/load-shape (str data-location "module_6.svg")))
    :7     (assoc state :current-shape (q/load-shape (str data-location "module_7.svg")))
    :up    (update state :shape-size + 10)
    :down  (update state :shape-size #(max (- % 5) 5))
    :left  (update state :shape-angle - 5)
    :right (update state :shape-angle + 5)
    state))

(q/defsketch P-2-1-1-04
  :middleware [m/fun-mode]
  :size [600 600]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
