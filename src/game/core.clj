(ns game.core
  (:gen-class))

(def measure-fns [])

(defn update-measures [measures e e']
  (reduce (fn [m f] (merge m (f m e e'))) measures measure-fns))

(defn draw [bag capital equity alloc]
  (let [amount (alloc capital equity), payoff (rand-nth capital)]
    (+ equity (* amount payoff))))

(defn go [env]
  (let [{:keys [bag capital equity alloc history measures]} env
        equity' (draw bag capital equity alloc)]
    (assoc env
      :equity equity' :history (conj history equity')
      :measures (update-measures measures equity equity'))))

(defn run [& args]
  (let [{:keys [bag capital alloc trial]} args]
    (first (drop trial
                 (iterate {:bag bag :capital capital :equity capital
                           :alloc alloc :history [capital] :measures {}}
                          go)))))
