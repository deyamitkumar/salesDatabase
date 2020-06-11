(ns sales
      (:require [clojure.string :as str]))


(defn elementSplit [stringer]
      (def tempStorage [])
      (doseq [temp stringer]
            (def splitedData (str/split temp #"\|"))
            (def tempStorage (conj tempStorage splitedData)))

      tempStorage
      )

(defn itemSplitByLine [str]
      (def tempStr (clojure.string/split-lines str))
      (elementSplit tempStr))



(defn loadFiles []
      (def customer (slurp "cust.txt"))
      (def product (slurp "prod.txt"))
      (def sales (slurp "sales.txt"))
      (def customerDatabase (itemSplitByLine customer))
      ;(println customerDatabase)
      ;(println ( type customerDatabase))

      (def sortedCustomerDB (sort customerDatabase))
      ;(println customerDatabase)

      (def productDatabase (itemSplitByLine product))
      ;(println productDatabase)
      (def sortedProductDB (sort productDatabase))

      (def salesDatabase (itemSplitByLine sales))
      ;(println salesDatabase)
      (def sortedSalesDB (sort salesDatabase))
      )


(defn getCustomerName [cna]
      (get (get customerDatabase (- (Integer/parseInt(get cna 1)) 1)) 1) )

(defn convPNameToID [pname]                                  ;pname is a string type
      ;(println pname)
      ;(println (type pname))

      (doseq [x productDatabase]
            ;(println "----inside doseq")
            ;(println "======" (type (get x 1)))
            ;(println (get x 1))
            (if (=(get x 1) pname) (def n  (Integer/parseInt (get x 0))) )
            )
      n
      )
(defn convCNameToID [cname]                                  ;cname is a string type
      ;(println cname)
      ;(println (type cname))

      (doseq [x customerDatabase]
            ;(println "----inside doseq")
            ;(println "======" (type (get x 1)))
            ;(println (get x 1))
            (if (=(get x 1) cname) (def n  (Integer/parseInt (get x 0))) )
            )
      n
      )

(defn convIdToPrice [id]
      (doseq [x productDatabase]
            ;(println "----inside doseq")
            ;(println "======" (type (get x 1)))
            ;(println (get x 1))
            (if (=(get x 0) id) (def n  (Float/parseFloat (get x 2))) )
            )
      n
      )



(defn getProductName [pna]
      (get (get productDatabase (- (Integer/parseInt(get pna 2)) 1)) 1) )
(defn menu []
      (loadFiles)


      (println "*** Sales Menu ***\n-----------------\n\n 1. Display Customer Table\n 2. Display Product Table\n 3. Display Sales Table \n 4.Total Sales for Customer \n 5.Total Count for Product\n 6.Exit\n")
      (println "Enter an Option?")
      (def inp (read-line))
      ;(println inp)
      (when (= "6" inp)
            (println "Good Bye")
            )
      (when (= "1" inp)
            (println "Display Customer Table")
            (doseq [x sortedCustomerDB]
                  (println (get x 0) ": ["(get x 1) "," (get x 2) ","(get x 3) "]")
                  )
            (menu)
            )
      (when (= "2" inp)
            (println "Display Product Table")
            (doseq [x sortedProductDB]
                  (println (get x 0) ": ["(get x 1) ","(get x 2) "]")
                  )
            (menu)
            )
      (when (= "3" inp)
            (println "Display Sales Table")
            (doseq [item salesDatabase]
                  (println (get item 0) ": ["
                           (getCustomerName item) ","
                           (getProductName item) ","
                           (get item 3)"]"
                           )
                  )
            (menu)
            )
      (when (= "4" inp)
            (def cost [])
            (println "Total Sales for Customer")
            (println "Enter a name: ")
            (def cname (read-line))
            (def custname #{})
            (doseq [x customerDatabase]
                  (def nametmp (get x 1))
                  (def custname (conj custname nametmp))
                  )
            ;(println custname)
            (if (contains? custname cname)
                  (do
                        (println "Name is here")
                        (def cn (convCNameToID cname))
                        (doseq [item salesDatabase]
                              (if (= (Integer/parseInt(get item 1))  cn)
                                    (do
                                          ; need a function for prod table when passed a id send the price
                                          (def price (* (Integer/parseInt(get item 3)) (convIdToPrice (get item 2)) ) ) ;next time price doesn't chage!!!!!
                                          (def cost (conj cost price))
                                          ;(def pdctmp (Integer/parseInt(get item 3)))
                                          ;(def pdc (conj pdc pdctmp))
                                          ;(conj pdc (get item 3))

                                          ;(assoc prodMap :name pname :count (get item 3))

                                          ;(println (get item 3))
                                          ;(println (type pdc) )
                                          )
                                    )
                              )
                        ;print details here
                        (println cname ": " "$" (format "%.2f" (reduce + cost)) )
                        )
                  (do
                        (println "Customer not found!")))
            (menu)
            )
      (when (= "5" inp)
            (def pdc [])
            ;(def prodMap {:name ""
            ;              :count ""})
            (println "Total Count for Product")
            (println "Enter the name of the Product: ")
            (def pname (read-line))

            (def productname #{})
            (doseq [x productDatabase]
                  (def pnametmp (get x 1))
                  (def productname (conj productname pnametmp))
                  )

            (if (contains? productname pname)
                  (do
                        ;(println "Name is here")
                        (def pn (convPNameToID pname) )
                        ;(println pn)
                        (doseq [item salesDatabase]
                              (if (= (Integer/parseInt(get item 2))  pn)
                                    (do
                                          ; increment count
                                          (def pdctmp (Integer/parseInt(get item 3)))
                                          (def pdc (conj pdc pdctmp))
                                          ;(conj pdc (get item 3))
                                          ;(assoc prodMap :name pname :count (get item 3))
                                          ;(println "Prod count is: ")
                                          ;(println (get item 3))
                                          ;(println (type pdc) )
                                          )
                                    )
                              )
                        ;print the total product count here
                        (println pname ":"  (reduce + pdc))
                       )
                  (do
                        (println "Product not found! ")))

            (menu)
            )


      )
(menu)