(ns game.core-test
  (:use [clojure test] [game core]))

(deftest test-drawdown
  (are [lp p t lv v p&t lp' p' t' lv']
    (= (drawdown {:last-peak lp :peak p :trough t :last-value lv} v)
       [p&t {:last-peak lp' :peak p' :trough t' :last-value lv'}])
    nil nil nil nil    10    nil        nil 10  nil 10

    nil nil nil 10     5     nil        nil 10  5   5
    nil nil nil 10     10    nil        nil 10  nil 10
    nil nil nil 10     20    nil        nil 20  nil 20

    nil 10  5   5      3     nil        nil 10  3   3
    nil 10  5   5      5     nil        nil 10  5   5
    nil 10  5   5      7     nil        nil 10  5   7
    nil 10  5   5      10    [10 5]     10  10  nil 10
    nil 10  5   5      20    [10 5]     10  20  nil 20

    nil 10  nil 10     7     nil        nil 10  7   7
    nil 10  nil 10     10    nil        nil 10  nil 10
    nil 10  nil 10     20    nil        nil 20  nil 20

    nil 10  5   7      3     nil        nil 10  3   3
    nil 10  5   7      5     nil        nil 10  5   5
    nil 10  5   7      6     nil        nil 10  5   6
    nil 10  5   7      7     nil        nil 10  5   7
    nil 10  5   7      8     nil        nil 10  5   8
    nil 10  5   7      10    [10 5]     10  10  nil 10
    nil 10  5   7      20    [10 5]     10  20  nil 20

    10  10  nil 10     5     nil        10  10  5   5
    10  10  nil 10     10    nil        10  10  nil 10
    10  10  nil 10     20    nil        10  20  nil 20

    10  20  nil 20     5     nil        10  20  5   5
    10  20  nil 20     10    nil        10  20  10  10
    10  20  nil 20     15    nil        10  20  15  15
    10  20  nil 20     20    nil        10  20  nil 20
    10  20  nil 20     25    nil        10  25  nil 25

    nil 20  10  15     5     nil        nil 20  5   5
    nil 20  10  15     10    nil        nil 20  10  10
    nil 20  10  15     12    nil        nil 20  10  12
    nil 20  10  15     15    nil        nil 20  10  15
    nil 20  10  15     17    nil        nil 20  10  17
    nil 20  10  15     20    [20 10]    20  20  nil 20
    nil 20  10  15     25    [20 10]    20  25  nil 25

    20  20  10  10     5     nil        20  20  5   5
    20  20  10  10     10    nil        20  20  10  10
    20  20  10  10     15    nil        20  20  10  15
    20  20  10  10     20    [20 10]    20  20  nil 20
    20  20  10  10     25    [20 10]    20  25  nil 25

    20  40  10  10     5     nil        20  40  5   5
    20  40  10  10     10    nil        20  40  10  10
    20  40  10  10     15    nil        20  40  10  15
    20  40  10  10     20    nil        20  40  10  20
    20  40  10  10     30    nil        20  40  10  30
    20  40  10  10     40    [40 10]    40  40  nil 40
    20  40  10  10     50    [40 10]    40  50  nil 50

    10  20  10  10     5     nil        10  20  5   5
    10  20  10  10     10    nil        10  20  10  10
    10  20  10  10     15    nil        10  20  10  15
    10  20  10  10     20    [20 10]    20  20  nil 20
    10  20  10  10     25    [20 10]    20  25  nil 25

    10  30  20  20     5     nil        10  30  5   5
    10  30  20  20     10    nil        10  30  10  10
    10  30  20  20     15    nil        10  30  15  15
    10  30  20  20     20    nil        10  30  20  20
    10  30  20  20     25    nil        10  30  20  25
    10  30  20  20     30    [30 20]    30  30  nil 30
    10  30  20  20     35    [30 20]    30  35  nil 35

    30  40  10  20     5     nil        30  40  5   5
    30  40  10  20     10    nil        30  40  10  10
    30  40  10  20     15    nil        30  40  10  15
    30  40  10  20     20    nil        30  40  10  20
    30  40  10  20     25    nil        30  40  10  25
    30  40  10  20     30    nil        30  40  10  30
    30  40  10  20     35    nil        30  40  10  35
    30  40  10  20     40    [40 10]    40  40  nil 40
    30  40  10  20     45    [40 10]    40  45  nil 45

    30  40  20  30     10    nil        30  40  10  10
    30  40  20  30     20    nil        30  40  20  20
    30  40  20  30     25    nil        30  40  20  25
    30  40  20  30     30    nil        30  40  20  30
    30  40  20  30     35    nil        30  40  20  35
    30  40  20  30     40    [40 20]    40  40  nil 40
    30  40  20  30     45    [40 20]    40  45  nil 45

    20  40  10  30     5     nil        20  40  5   5
    20  40  10  30     10    nil        20  40  10  10
    20  40  10  30     15    nil        20  40  10  15
    20  40  10  30     20    nil        20  40  10  20
    20  40  10  30     25    nil        20  40  10  25
    20  40  10  30     30    nil        20  40  10  30
    20  40  10  30     35    nil        20  40  10  35
    20  40  10  30     40    [40 10]    40  40  nil 40
    20  40  10  30     45    [40 10]    40  45  nil 45))
