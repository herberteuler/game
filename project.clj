(defproject game "0.1"
  :description "trading games"
  :url ""
  :license {:name "GNU GPL"
            :url "http://www.gnu.org/copyleft/gpl.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :aot :all
  :main game.core
  :global-vars {*warn-on-reflection* true}
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
