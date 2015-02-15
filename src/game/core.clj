(ns game.core
  (:import [javafx.application Application Platform]
           [javafx.stage Stage])
  (:gen-class :extends javafx.application.Application))

(defn profit [m e e' env]
  (let [{:keys [capital]} env
        x (- e' capital)]
    {:net-profit x :gain% (/ x capital 0.01)}))

(defn drawdown [context value]
  (let [{:keys [last-peak peak trough last-value]} context]
    (cond (and (nil? peak) (nil? trough))
          (if (or (nil? last-value) (>= value last-value))
            [nil {:last-peak nil :peak value
                  :trough nil :last-value value}]
            [nil {:last-peak nil :peak last-value
                  :trough value :last-value value}])

          (< value last-value)
          [nil (assoc context
                 :trough (if (nil? trough) value (min trough value))
                 :last-value value)]

          (< value peak)
          [nil (assoc context :last-value value)]

          (nil? trough)
          [nil (assoc context :peak value :last-value value)]

          (or (nil? last-peak) (>= value last-peak))
          [[peak trough]
           {:last-peak peak :peak value :trough nil :last-value value}]

          :otherwise
          [nil (assoc context :peak value :last-value value)])))

(defn max-drawdown [m e e' env]
  (let [{:keys [max-drawdown max-drawdown% drawdown-context]
         :or {max-drawdown 0 max-drawdown% 0
              drawndown-context {:last-value e}}} m
        [[p t] ctx] (drawdown drawdown-context e')]
    (merge {:drawdown-context ctx}
           (when p
             (let [d (- p t)]
               (when (> d max-drawdown)
                 {:max-drawdown d :max-drawdown% (/ d p 0.01)}))))))

(defn streak [cmp stk longest-stk]
  (fn [m e e' env]
    (if (cmp e' e)
      {stk (inc (get m stk 0))}
      {stk 0 longest-stk (max (get m stk 0) (get m longest-stk 0))})))

(def winning-streak (streak > :winning-streak :longest-winning-streak))

(def losing-streak (streak < :losing-streak :longest-losing-streak))

(defn equity [cmp eq eq%]
  (fn [m e e' env]
    (let [{:keys [capital]} env]
      (when (cmp e' (get m eq capital))
        {eq e' eq% (/ e' capital 0.01)}))))

(def max-equity (equity > :max-equity :max-equity%))

(def min-equity (equity < :min-equity :min-equity%))

(def measure-fns [profit max-drawdown winning-streak losing-streak
                  max-equity min-equity])

(defn update-measures [measures e e' env]
  (reduce (fn [m f] (merge m (f m e e' env))) measures measure-fns))

(defn draw [bag capital equity alloc]
  (let [amount (alloc capital equity), payoff (rand-nth bag)]
    (+ equity (* amount payoff))))

(defn go [env]
  (let [{:keys [bag capital equity alloc history measures turn]
         :or {turn 0}} env
        equity' (draw bag capital equity alloc)]
    (assoc env
      :turn (inc turn) :equity equity' :history (conj history equity')
      :measures (update-measures measures equity equity' env))))

(defn run [& args]
  (let [{:keys [bag capital alloc trial]} args]
    (loop [sq (iterate go
                       {:bag bag :capital capital :equity capital
                        :alloc alloc :history [capital] :measures{}})
           k 0]
      (let [[x & sq'] (seq sq)]
        (if (or (>= k trial) (<= (:equity x) 0))
          x
          (recur sq' (inc k)))))))

(def bag-1 (vec (concat
                 (repeat 50 -1)
                 (repeat 10 -2)
                 (repeat 4 -3)
                 (repeat 20 1)
                 (repeat 10 5)
                 (repeat 3 10)
                 (repeat 3 20))))

(defn alloc-fix-amount [n]
  (let [n' (* n 1.0)]
    (fn [capital equity]
      n')))

(defn alloc-fix-capital-percent [n]
  (fn [capital equity]
    (/ capital n 1.0)))

(defn alloc-fix-equity-percent [n]
  (fn [capital equity]
    (/ equity n 1.0)))

(def ^Stage primary-stage)

(defn -start [this ^Stage stage]
  (alter-var-root #'primary-stage (constantly stage)))

(defn start-app []
  (future
    (let [args (make-array String 0)]
      (Platform/setImplicitExit false)
      (Application/launch game.core args))))

(defn stop-app []
  (Platform/exit))
