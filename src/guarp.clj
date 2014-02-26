

;; around - I need picture creation
;; need agents -> having there scanned environment - have their fun :)
;; -> flood filll - some kind of state - coming from? :O 
;;    effort - least effort - can

;; torso and head trajectories on the plane (x,y) for GUAR subject. He performed 480 trajectories (40 positions X 12 directions). 

;; THE TRAJECTORY DATA - for guarpolo - fuckermakker 
;; time pelvis X pelivs Y torso Theta torso X torso Y torso Theta head X  head Y head Theta
(defn trajp-init [tim px py pt tx ty tt hx hy ht]
  {:tim tim
   :px px :py py :pt pt 
   :tx tx :ty ty :tt tt
   :hx hx :hy hy :ht ht})
;; x y theta 
(defn goalp-init [gx gy gt]
  {:gx gx :gy gy :gt gt})
;; the file name has the Posnum - numAttempt - postheta position num
(defn filp-init [pos-num attempt-num pos-theta]
  {:pos-num pos-num 
   :attempt-num attempt-num 
   :pos-theta pos-theta})

;; checking the velocity hypothesis - we are around for that
(defn str->goalp [s]
  (apply goalp-init (map read-string (clojure.string/split s #" "))))
(defn str->trajp [s]
  (apply trajp-init (map read-string (clojure.string/split s #" "))))
(defn strs->trajps [ss]
  (map str->trajp ss))
(defn ppart-str->filp [s] 
  (apply filp-init (map read-string (clojure.string/split s #"_"))))

;; the defining joint setting 
(defn joint-init [trajps goalp filp]
  {:trajps trajps
   :goalp goalp
   :filp filp})

;; slurping line-seq 
(def guarp-pth "/home/files/prog/clj/guarp/data/GUARPtorso")
(defn str-line-slurp [nam]
  (with-open [rdr (clojure.java.io/reader (str guarp-pth "/" nam))]
    (doall (line-seq rdr))))
(defn all-goal-nams []
  (remove #(or (re-find #"P6" %) 
               (re-find #"P21" %) 
               (re-find #"P38" %))
          (str-line-slurp "allGoals.txt")))


(defn str-goalp? [s]
  (re-find #"goal" s))
(defn str-trajp? [s]
  (re-find #"traj" s))

(defn nam->ppart [nam]
  (subs nam 4))
(defn ppart->chop-Ptxt [s]
  (subs s 1 (- (count s) 4)))
(defn nam->chopped-ppart [nam]
  (ppart->chop-Ptxt (nam->ppart nam)))

(defn all-pparts []
  (map nam->chopped-ppart (all-goal-nams)))

(defn ppart->goalp [ppart]
  (str->goalp (first (str-line-slurp (str "goalP" ppart ".txt")))))
(defn ppart->trajps [ppart]
  (strs->trajps (str-line-slurp (str "trajP" ppart ".txt"))))
(defn ppart->filp [pprt]
  (ppart-str->filp pprt))
(defn ppart->joint [ppart]
  (joint-init (ppart->trajps ppart)
              (ppart->goalp ppart)
              (ppart->filp ppart)))

(defn pparts->joints [pparts]
  (map ppart->joint pparts))
(defn all-joints []
  (pparts->joints (all-pparts)))

(defn joint-drop-trajps [j nth]
  (assoc j :trajps (take-nth nth (:trajps j))))
(defn joints->dropped-joints [js nth]
  (for [j js] (joint-drop-trajps j nth)))

  
;; FILTERS AND ALL SORT 
(defn joints->sort-by-pos-num [js]
  (sort (fn [j0 j1] (< (:pos-num (:filp j0)) (:pos-num (:filp j1))))
        js))                                  
(defn joints->filter-by-theta [js deg]  
  (filter #(= (:pos-theta (:filp %)) deg) js))
(defn joints->filter-by-pos-num [js num]  
  (filter #(= (:pos-num (:filp %)) num) js))



;; CREATING THE GOALP PICTURES and stuff - for ros compatibility also 
;; for the goal - unfortunately - you will have to tell - so include in the file 
;; 10 meter high -> the robot starts from the end .. 
;; -4 +4 meter  ->  so that's 
;; ROTATION - and creating stuff around - 
;; BUT BECAUSE I USE Y FIRST - the whole stuff is TURNED AROUND - GEE :O 
(defn rotate-pos-unit [[p0 p1] rad]        
  [(- (* p0 (Math/cos rad)) (* p1 (Math/sin rad)))
   (+ (* p0 (Math/sin rad)) (* p1 (Math/cos rad)))])
;; (defn rotate-pos-unit [[p0 p1] rad]        
;;   [(+ (* p0 (Math/sin rad)) (* p1 (Math/cos rad)))
;;    (- (* p0 (Math/cos rad)) (* p1 (Math/sin rad)))])
(defn rotate-poss-rad [poss rad]
  (doall (map rotate-pos-unit poss (repeat rad))))
(defn rotate-poss-deg [poss deg]
  (let [rad (Math/toRadians deg)]
    (rotate-poss-rad poss rad)))

;; porch - meters x meters - square - great :)
;; guarp is in millis so it's - 1000 x 1000 
(defn line-dots [p0 p1 n]
  (let [diff (vec-vec p1 p0)
        incr (vec*scl diff (/ 1.0 (dec n)))]
    (for [i (range 0 n)]
      (vec+vec p0 (vec*scl incr i)))))                      
(def porch-leftside (line-dots [-0.5 -0.5] [-0.5 0.5] 12))
(def porch-rightside (line-dots [0.5 -0.5] [0.5 0.5] 12))
(def porch-bottomside (line-dots [-0.5 -0.5] [0.5 -0.5] 12))
(def porch-sides (rotate-poss-deg
                  (map vec*scl 
                       (concat porch-leftside porch-rightside porch-bottomside)
                       (repeat 1000))
                  90))

(defn goalp->porch-poss [g]
  (let [pos0 [(:gx g) (:gy g)]
        deg (:gt g)]
    (doall (map vec+vec (repeat pos0) (rotate-poss-deg porch-sides deg)))))

;; how is this possible - minus or not minus .. 
;; I have both - and that's quite mind-boggling - that's bad .. we should have sometihng .. 

