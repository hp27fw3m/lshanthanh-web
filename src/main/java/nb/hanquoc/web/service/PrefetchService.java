package nb.hanquoc.web.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class PrefetchService {
    private static HashMap<String, HashSet<String>> configMap = new HashMap<>();

    private void updateMap() throws FileNotFoundException, IOException, CsvValidationException {
        configMap.clear();

        try (CSVReader reader = new CSVReader(
                new InputStreamReader(new FileInputStream("./conf/prefetch.txt"), "UTF-8"))) {
            String[] nextRecord;
            while ((nextRecord = reader.readNext()) != null) {
                HashSet<String> list = configMap.get(nextRecord[0]);
                if (list == null) {
                    list = new HashSet<>();
                    configMap.put(nextRecord[0], list);
                }
                list.add(nextRecord[1]);
            }
        }

    }

    public ArrayList<String> getAddressList(String key) {
        
        if (!configMap.containsKey(key)) {
            try {
                updateMap();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
        }
        ArrayList<String> arrayList = new ArrayList<>(configMap.get(key));
        Collections.sort(arrayList);
        return arrayList;
    }
}
