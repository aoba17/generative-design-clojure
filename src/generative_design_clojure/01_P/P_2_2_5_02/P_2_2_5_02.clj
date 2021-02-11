(ns generative-design-clojure.01-P.P-2-2-5-02.P-2-2-5-02
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def data-location "src/generative_design_clojure/01_P/P_2_2_5_02/data/")

(def max-count 5000)
(def min-radius 3)
(def max-radius 50)
(def is-mouse-pressed (atom false))

(defn setup []
  (q/no-fill)
  (q/cursor :cross)
  {:freeze        false
   :current-count 1
   :x             [200]
   :y             [100]
   :r             [50]
   :closest-index [0]
   :mouse-rect    30
   :show-svg      true
   :show-line     false
   :show-circle   false
   :module1       (q/load-shape (str data-location "01.svg"))
   :module2       (q/load-shape (str data-location "02.svg"))})

(defn update-state [{:keys [freeze current-count mouse-rect x y r]
                     :as   state}]
  (if-not freeze
    (let [tx           (if @is-mouse-pressed
                         (q/random (- (q/mouse-x) (/ mouse-rect 2))
                                   (+ (q/mouse-x) (/ mouse-rect 2)))
                         (q/random max-radius (- (q/width) max-radius)))
          ty           (if @is-mouse-pressed
                         (q/random (- (q/mouse-y) (/ mouse-rect 2))
                                   (+ (q/mouse-y) (/ mouse-rect 2)))
                         (q/random max-radius (- (q/height) max-radius)))
          tr           (if @is-mouse-pressed
                         1
                         min-radius)
          intersection (atom false)]
      (dotimes [i current-count]
        (when (< (q/dist tx ty (nth x i) (nth y i))
                 (+ tr (nth r i)))
          (reset! intersection true)))
      (if-not @intersection
        (let [t-radius (atom (q/width))
              closest  (atom 0)]
          (dotimes [i current-count]
            (let [d (q/dist tx ty (nth x i) (nth y i))]
              (when (> @t-radius (- d (nth r i)))
                (reset! t-radius (- d (nth r i)))
                (reset! closest i))))
          (when (> @t-radius max-radius)
            (reset! t-radius max-radius))
          (-> state
              (update :closest-index conj @closest)
              (update :x conj tx)
              (update :y conj ty)
              (update :r conj @t-radius)
              (update :current-count inc)))
        state))
    state))

(defn draw [{:keys [current-count
                    mouse-rect
                    x y r
                    closest-index
                    show-svg
                    show-line
                    show-circle
                    module1
                    module2]}]
  (q/shape-mode :center)
  (q/ellipse-mode :center)
  (q/background 255)
  (dotimes [i current-count]
    (let [radius   (nth r i)
          diameter (* radius 2)]
      (q/with-translation [(nth x i) (nth y i)]
        (q/rotate (q/radians (nth r i)))
        (when show-svg
          (if (= radius max-radius)
            (q/shape module1 0 0 diameter diameter)
            (q/shape module2 0 0 diameter diameter)))
        (when show-circle
          (q/stroke 0)
          (q/stroke-weight 1.5)
          (q/ellipse 0 0 diameter diameter)))
      (when show-line
        (q/stroke 150)
        (q/stroke-weight 1)
        (q/line (nth x i)
                (nth y i)
                (nth x (nth closest-index i))
                (nth y (nth closest-index i))))))

  (when @is-mouse-pressed
    (q/stroke 255 200 0)
    (q/stroke-weight 2)
    (q/rect (- (q/mouse-x) (/ mouse-rect 2))
            (- (q/mouse-y) (/ mouse-rect 2))
            mouse-rect mouse-rect))

  (when (>= current-count max-count)
    (q/no-loop)))

(defn key-released [state event]
  (case (:key event)
    :f    (update state :freeze not)
    :1    (update state :show-svg not)
    :2    (update state :show-line not)
    :3    (update state :show-circle not)
    :up   (update state :mouse-rect + 4)
    :down (update state :mouse-rect - 4)
    state))

(defn mouse-pressed [state _]
  (reset! is-mouse-pressed true)
  state)

(defn mouse-released [state _]
  (reset! is-mouse-pressed false)
  state)

(q/defsketch P-2-2-5-02
  :middleware [m/fun-mode]
  :size [800 800]
  :setup setup
  :update update-state
  :draw draw
  :mouse-pressed mouse-pressed
  :mouse-released mouse-released
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
