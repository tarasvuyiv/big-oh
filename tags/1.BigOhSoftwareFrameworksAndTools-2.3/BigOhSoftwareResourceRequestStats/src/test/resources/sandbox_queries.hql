select 
distinct 
r, avg(rr.millisecondsToServiceRequest) 
from 
Resource r 
left outer join 
r.resourceRequests rr 
group by 
r 
order by 
avg(rr.millisecondsToServiceRequest) desc 

