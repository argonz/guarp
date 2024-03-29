
;; using INCANTER for plotting the scheme - we are around and all hungry 
(use '(incanter core stats charts)) 

;; about colouring 
;; (set-stroke-color chart color & options)
;; (doto (line-chart [:a :b :c :d] [10 20 5 35])
;;     (set-stroke :width 4 :dash 5)
;;     (set-stroke-color java.awt.Color/blue)
;;     view)

(defn poss->scatter-view [poss]
  (let [xs (map first poss)
        ys (map second poss)]
    (view (scatter-plot xs ys))))

(defn trajps->poss [ts]
  (for [t ts] [(:tx t) (:ty t)]))
(defn trajps->txs-tys [ts]
  [(map :tx ts) (map :ty ts)])
(defn joints->txs-tys [js]
  (reduce (fn [[x0 y0] [x1 y1]] [(into x0 x1) (into y0 y1)]) (map trajps->txs-tys (map :trajps js))))

(defn trajps->scatter-view [ts]  
  (let [[xs ys] (trajps->txs-tys ts)]
    (view (scatter-plot xs ys))))
(defn joint->scatter-view [j]
  (trajps->scatter-view (:trajps j)))
(defn joints->scatter-view [js]
  (let [[xs ys] (joints->txs-tys js)]
    (doto (scatter-plot xs ys)
      (set-stroke :width 2 :dash 1)
      view)))

(defn joint->trajp-goalp-poss [j]
  (into (trajps->poss (:trajps j)) 
        (goalp->porch-poss (:goalp j))))
(defn println-goalp-trajplast [j]
  (let [g (:goalp j)
        t (last (:trajps j))]
    (println "new joint")
    (println (:gx g) (:gy g) "  th:" (:gt g))
    (println (:tx t) (:ty t) "  ph:" (:pt t))))
(defn joint->trajp-goalp-scatter-view [j]
  (println-goalp-trajplast j)
  (poss->scatter-view (joint->trajp-goalp-poss j)))
        
                                 
  

;; different colours - for the different plottings that's gonna - should be :O 
;; ohhhoohohoo :) - we will be more 





;; VIEWING THE DOING ALL THE SHIT AROUND - we will have more about it 
(def *joints* (joints->sort-by-pos-num (all-joints)))
(def *6joints* (joints->dropped-joints *joints* 6))
(def *8joints* (joints->dropped-joints *joints* 8))
(def *16joints* (joints->dropped-joints *joints* 16))

(joints->scatter-view (take 10 (joints->filter-by-theta  *8joints* 240)))

;; (joints->scatter-view (take 12 *8joints*))
;; (joints->scatter-view (take 1 *8joints*))
;; (joints->scatter-view (take-nth 8 (filter-joints-pos-theta *8joints* 270)))

;; painting the maps .. that's gonna be great around :) 
;; 

(defn println-goalp-trajplast [j]
  (let [g (:goalp j)
        t (last (:trajps j))]
    (println (:gx g) (:gy g) "  th:" (:gt g))
    (println (:tx t) (:ty t) "  ph:" (:pt t))))


;; (joint->trajp-goalp-scatter-view (nth *joints* 239))

;; ;; 60 deg 
;; (joint->trajp-goalp-scatter-view (nth *joints* 190))
;; ;; 140 deg 
;; (joint->trajp-goalp-scatter-view (nth *8joints* 290))
;; ;; 
;; (joint->trajp-goalp-scatter-view (nth *8joints* 390))
;; (joint->trajp-goalp-scatter-view (nth *8joints* 380))
;; (joint->trajp-goalp-scatter-view (nth *8joints* 370))

;; how this is all about - thats gonna be around 
;; having trajectory-end 

;; (joints->scatter-view (take 1 *8joints*))
;; (poss->scatter-view (goalp->porch-dots (:goalp (first *8joints*))))
(joint->trajp-goalp-scatter-view (nth *joints* 220))
(joint->trajp-goalp-scatter-view (nth *8joints* 220))


;; I HAVE TO GO FOR THAT - it's like sucking it in - that its around the 
;; that this is a different corner - it will be more - ohh yeah :) 

;; so that's around - we will have more that's around 
;; that's how it should be

;; okay so we will see some how this should look like :) 

