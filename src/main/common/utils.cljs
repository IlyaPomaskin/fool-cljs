(ns common.utils)

(defn find-item [pred coll]
  (reduce
   (fn [acc item]
     (if (not (nil? acc))
       acc
       (if (pred item)
         item
         acc)))
   nil
   coll))

(defn find-item-index [pred coll]
  (nth
   (find-item
    (fn [[_index item]] (pred item))
    (map-indexed (fn [index item] [index item]) coll))
   0))

(defn first-error [pred-pairs]
  (reduce
   (fn [acc-error [fn error]]
     (if (nil? acc-error)
       (when (apply fn nil) error)
       acc-error))

   nil

   pred-pairs))