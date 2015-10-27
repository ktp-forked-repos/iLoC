


(define (problem tsp-9)
(:domain tsp)
(:objects p1 p2 p3 p4 p5 p6 p7 p8 p9 )
(:init
(at p1)
(connected p1 p4)
(connected p4 p1)
(connected p1 p6)
(connected p6 p1)
(connected p1 p7)
(connected p7 p1)
(connected p3 p2)
(connected p2 p3)
(connected p3 p4)
(connected p4 p3)
(connected p3 p9)
(connected p9 p3)
(connected p5 p3)
(connected p3 p5)
(connected p5 p7)
(connected p7 p5)
(connected p5 p8)
(connected p8 p5)
(connected p5 p9)
(connected p9 p5)
(connected p7 p4)
(connected p4 p7)
(connected p7 p5)
(connected p5 p7)
(connected p9 p5)
(connected p5 p9)
(connected p9 p6)
(connected p6 p9)
)
(:goal
(and
(visited p1)
(visited p2)
(visited p3)
(visited p4)
(visited p5)
(visited p6)
(visited p7)
(visited p8)
(visited p9)
)
)
)

