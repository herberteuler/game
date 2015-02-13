(ns game.core
  (:gen-class))

(defn profit [m e e' env]
  (let [x (- e' e)
        {:keys [capital]} env]
    {:net-profit x :%-gain (* (/ x capital) 100.0)}))

(defn max-drawdown [m e e' env]
  (when (< e' e)
    (let [x (- e' e)
          {:keys [capital]} env
          dd (* (/ x capital) 100.0)
          {:keys [max-drawdown] :or {max-drawdown 0}} m]
      (when (< dd max-drawdown)
        {:max-drawdown dd}))))

(defn streak [cmp stk max-stk]
  (fn [m e e' env]
    (if (cmp e' e)
      {stk (inc (get m stk 0))}
      {stk 0 max-stk (max (get m stk) (get m max-stk 0))})))

(def winning-streak (streak > :winning-streak :longest-winning-streak))

(def losing-streak (streak < :losing-streak :longest-losing-streak))

(def measure-fns [profit max-drawdown winning-streak losing-streak])

(defn update-measures [measures e e' env]
  (reduce (fn [m f] (merge m (f m e e' env))) measures measure-fns))

(defn draw [bag capital equity alloc]
  (let [amount (alloc capital equity), payoff (rand-nth capital)]
    (+ equity (* amount payoff))))

(defn go [env]
  (let [{:keys [bag capital equity alloc history measures]} env
        equity' (draw bag capital equity alloc)]
    (assoc env
      :equity equity' :history (conj history equity')
      :measures (update-measures measures equity equity' env))))

(defn run [& args]
  (let [{:keys [bag capital alloc trial]} args]
    (first (drop trial
                 (iterate {:bag bag :capital capital :equity capital
                           :alloc alloc :history [capital] :measures {}}
                          go)))))
