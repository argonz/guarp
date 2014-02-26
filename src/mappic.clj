
;; TODO - really much here - that's gonna be 
;; generating the 

;; this will be the transformation - great :) 

;; transformations - that's gonna be allright - that will go on the shit 
;; that's gonna so long - that

;; so that's all you have - so around 
;; we are in d 


;; we have 1 to 1 mapping - so we will have all the 1000 ms 
;; that's gonna be allright - that's gonna be something .. that's gonna be somewhere :) - that's gonna be - we will see more about - that's gonna be somewhere .. that's the spirit so around - we will be more there :) - that can be :) 
;; we will see that .. :) 


;; we are setting hardcoded for these 
;; in millimeter
(def guarp-base-plane [8000 9000])
(def guarp-half-offset [4000 0])

(def mappic-scale 0.01)
(defn mappic-base-plane [] (vec*scl guarp-base-plane mappic-scale))
;;(def mappic-base-plane (map vec*scl guarp-base-plane mappic-scale))


(defn porch-pos->vobjs [p]
  (vrect-pos-init (vec+vec p guarp-half-offset) [120 120] [0 0 0]))
(defn porch-poss->vobjs [ps]
  (doall (map porch-pos->vobjs ps)))
(defn goalp->porch-vobjs [g]
  (porch-poss->vobjs (goalp->porch-poss g)))
(defn joint->porch-vobjs [j]
  (goalp->porch-vobjs (:goalp j)))

;; 0 - 9 meter long    -> this is going to 9000 
;; -4 - +4 meter wide  -> this is going to -4000 +4000 - so that's quite much 
(defn mappic-base-vobjs [] 
  [(vrect-corner-init [0 0] guarp-base-plane [255 255 255])
   ;; (vrect-corner-init [0 0] [8000 100] [0 0 0])
   ;; (vrect-corner-init [0 0] [100 9000] [0 0 0])
   ;; (vrect-corner-init [7900 0] [0 8000] [0 0 0])
   ;; (vrect-corner-init [0 0] [8000 0] [0 0 0])
  ])

(defn joint->vobjs [j]
  (vobjs->scale (concat (mappic-base-vobjs)
                        (joint->porch-vobjs j))
                mappic-scale))
(defn joint->show-pic [j]
  (vobjs->show (joint->vobjs j) (mappic-base-plane)))

;; creating the saving routie 
(defn joint->save-pic [j pth]
  (let [filp (:filp j)
        [d0 d1] (map int (mappic-base-plane))
        fils (str pth "/"
                  "mappic" 
                  d0 "x" d1
                  "_pn" (:pos-num filp) 
                  "_an" (:attempt-num filp)
                  "_th" (:pos-theta filp)
                  ".png")]
    (vobjs->save-image (joint->vobjs j) (mappic-base-plane) fils)))

(defn joints->save-pics [js pth]
  (for [j js] (joint->save-pic j pth)))


;; SOME 
;; (joint->vobjs (nth *joints* 239))
;; (joint->show-pic (nth *joints* 239))
;; (joint->save-pic (nth *joints* 239) "/home/mate/Desktop")

;; ALL PIC
;; (joints->save-pics *joints* "/home/mate/clj/guarp/data/mappics")



