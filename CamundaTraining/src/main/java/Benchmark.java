import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Benchmark {
    
    private static Benchmark instance;    
    private Map<String, Measure> measures;
    
    private Benchmark() {
        measures = new LinkedHashMap<>();
    }
    
    public static Benchmark newInstance() {
        return new Benchmark();
    }
    
    public static Benchmark getDefaultInstance() {
        if (null == instance) {
            instance = new Benchmark();
        }
        return instance;
    }
    
    public void start(String action) {
        measures.put(action, Measure.init(action, System.nanoTime(), Runtime.getRuntime().totalMemory(), Runtime.getRuntime().freeMemory()));   
    }
    
    public void end(String action) throws BenchmarkException {
        if (!measures.containsKey(action)) {
            throw new BenchmarkException(action + " action not started");
        } else {
            Measure m = measures.get(action);
            if (m.hasEnded()) {
                throw new BenchmarkException("Action already finished");
            } else {
                m.end(System.nanoTime(), Runtime.getRuntime().totalMemory(), Runtime.getRuntime().freeMemory());
            }
        }
    }
    
    public String log() {        
        String title = "\nBENCHMARK LOG\n\n################################";
        String body = String.join("\n", measures.values().stream().map(i -> i.toString()).collect(Collectors.toList())); 
        
        measures.clear();
        
        return title + body;
    }
    
    
    public static class Measure {
        
        private static Long divideBytesToMb = 1024l * 1024l;
        
        private String name;
        
        private Long startTime;
        private Long endTime;
        
        private Long startTotalMemory;
        private Long endTotalMemory;
        
        private Long startFreeMemory;
        private Long endFreeMemory;
        
        private Measure(String name, Long startTime, Long startTotalMemory, Long startFreeMemory) {
            this.name = name;
            this.startTime = startTime;
            this.startTotalMemory = startTotalMemory;
            this.startFreeMemory = startFreeMemory;
        }
        
        public static Measure init(String name, Long startTime, Long startTotalMemory, Long startFreeMemory) {
            return new Measure(name, startTime, startTotalMemory, startFreeMemory);
        }
        
        public void end(Long endTime, Long endTotalMemory, Long endFreeMemory) {
            this.endTime = endTime;
            this.endTotalMemory = endTotalMemory;
            this.endFreeMemory = endFreeMemory;
        }
        
        public String toString() {
            if (hasEnded()) {
                return "\nMeasure: " + name  
                        + "\n--------------------------------"
                        + "\nDuration: " + TimeUnit.MILLISECONDS.convert(duration(), TimeUnit.NANOSECONDS) + "ms"
                        + "\nStart memory used: " + (startTotalMemory - startFreeMemory) / divideBytesToMb + "mb"                        
                        + "\nEnd memory used: " + (endTotalMemory - endFreeMemory) / divideBytesToMb  + "mb"                        
                        + "\nMemory change: " + (memoryChange() > 0 ? "+" : "") + memoryChange() / divideBytesToMb + "mb"
                        + "\nStart total memory: " + startTotalMemory / divideBytesToMb + "mb"
                        + "\nEnd total memory: " + endTotalMemory / divideBytesToMb  + "mb"
                        + "\n################################";
            } else {
                return "";
            }
        }
        
        public Boolean hasEnded() {
            return null != endTime;
        }
        
        public Long duration() {
            if (null != endTime)
                return endTime - startTime;
            return 0l;
        }
        
        public Long memoryChange() {
            if (null != endFreeMemory)
                return ((endTotalMemory - endFreeMemory) - (startTotalMemory - startFreeMemory));
            return 0l;
        }
                
    }
    
    
    public class BenchmarkException extends Exception {
        
        private static final long serialVersionUID = -1794578529783454972L;

        public BenchmarkException(String message) {
            super(message);
        }
    }
    
//    private List<Measure> measures;
//    private static Benchmark instance;
//    
//    private Benchmark() {
//        measures = new ArrayList<>();
//    }
//    
//    public static Benchmark getInstance() {
//        if (null == instance)
//            instance = new Benchmark();
//        return instance;
//    }
//    
//    public void collectDataForAction(String action) {
//        measures.add(new Measure(action, System.nanoTime(), Runtime.getRuntime().freeMemory(), Runtime.getRuntime().totalMemory()));
//    }    
//    
//    public String log() {
//        
//        String log = "";
//        
//        for (int i = 0; i < measures.size(); i++) {
//            
//            Measure m = measures.get(i);
//            Long nextActionTime = measures.size() < i + 1 ? measures.get(i + 1).getCurrentTime() : System.nanoTime();
//            
//            log = m.getAction() + " - Duração: " + (nextActionTime - m.getCurrentTime()) + " - Memória total: " + m.getTotalMemory() + " / Memória utilizada: " + (m.getTotalMemory() - m.getFreeMemory());
//            log += "\n";            
//        }       
//        
//        return log;
//    }
//    
//    public class Measure {
//        
//        private String action;
//        private Long currentTime;        
//        private Long totalMemory;
//        private Long freeMemory;
//                
//        public Measure(String action, Long currentTime, Long totalMemory, Long freeMemory) {
//            super();
//            this.action = action;
//            this.currentTime = currentTime;
//            this.totalMemory = totalMemory;
//            this.freeMemory = freeMemory;
//        }
//        
//        public String getAction() {
//            return action;
//        }
//        public void setAction(String action) {
//            this.action = action;
//        }
//        public Long getCurrentTime() {
//            return currentTime;
//        }
//        public void setCurrentTime(Long currentTime) {
//            this.currentTime = currentTime;
//        }
//        public Long getTotalMemory() {
//            return totalMemory;
//        }
//        public void setTotalMemory(Long totalMemory) {
//            this.totalMemory = totalMemory;
//        }
//        public Long getFreeMemory() {
//            return freeMemory;
//        }
//        public void setFreeMemory(Long freeMemory) {
//            this.freeMemory = freeMemory;
//        }        
//    }
}
