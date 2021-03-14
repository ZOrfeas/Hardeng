### Αλλαγές στα activity και sequence diagrams:

- Στο Charging experience activity diagram, εμφανίζονται μόνο οι διαθέσιμοι σταθμοί, άρα δεν δείχνει το “Show expected wait time”, και την κατάσταση του κάθε σταθμού.
- Στο Energy Monitoring activity diagram, ο driver δεν έχει την δυνατότητα να δει όλα τα actions, αλλά μόνο το “Show cost per Km for given duration”, και ο admin αντίστοιχα μπορεί να δει όλα τα actions εκτός αυτού.
- Στο Station monitoring and administration activity diagram, ο admin αντί για user data, έχει την δυνατότητα είτε να βλέπει είτε να αλλάζει τις πληροφορίες των price policy, και έχει πάντα το δικαίωμα αλλαγής των δεδομένων.
- Στο activity diagram της "Πληρωμής", αφαιρέθηκε η δυνατότητα να αποθηκεύεται ο προηγούμενος τρόπος πληρωμής.
- Στο activity diagram της "Αγορά πακέτου/προσφοράς (Υπηρεσίες καταστήματος)", αφαιρέθηκε η δυνατότητα για “Add product to cart”.

### Αλλαγές στο Deployment diagram:

- Groupαρίστηκαν μαζί όλοι οι server, γιατί εν τέλη όλες οι υπηρεσίες τρέχουν στο ίδιο μηχάνημα.
- Αλλαγή από Nginx σε Apache.
- Αφαίρεση των firewall. ( :^) )
- Μικροδιορθώσεις στα port numbers

### Αλλαγές στο Component diagram

- Ακριβέστερη σχεδίαση, αφού πλέον γνωρίζουμε τα ονόματα των component και τις εξαρτήσεις τους

### Αλλαγές στα SRS|STRS

- Προσθήκη των νέων διαγραμμάτων
