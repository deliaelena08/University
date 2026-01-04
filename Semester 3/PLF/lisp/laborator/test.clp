(format t "Acesta este un test pentru load!~%")
(defun my-load (file)
  (with-open-file (stream file)
    (loop for form = (read stream nil)
          while form
          do (eval form))))
		  
(my-load "D:/facultate/Semestrul 3/PLF/lisp/laborator/subiect3.clp")


