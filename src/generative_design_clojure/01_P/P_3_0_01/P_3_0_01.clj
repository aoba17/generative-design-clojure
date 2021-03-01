(ns generative-design-clojure.01-P.P-3-0-01.P-3-0-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/background 255)
  (q/fill 0)
  (q/text-font (q/create-font "ArialMT" 12))
  (q/text-align :center :center)
  {:letter "A"})

(defn draw [_])

(defn mouse-moved [state {:keys [x y]}]
  (q/background 255)
  (q/text-size (max 1
                    (* (- x
                          (/ (q/width) 2))
                       5)))
  (q/text (:letter state) (/ (q/width) 2) y)
  state)

(defn mouse-dragged [state {:keys [x y]}]
  (q/text-size (max 1
                    (* (- x
                          (/ (q/width) 2))
                       5)))
  (q/text (:letter state) (/ (q/width) 2) y)
  state)

(defn key-released [state {:keys [key key-code raw-key]}]
  (when (= key :control)
    (util/save-frame))
  (if (> key-code 32)
    (assoc state :letter (str raw-key))
    state))

(q/defsketch P-3-0-01
  :middleware [m/fun-mode]
  :size [800 800]
  :setup setup
  :draw draw
  :mouse-moved mouse-moved
  :mouse-dragged mouse-dragged
  :key-released key-released)
