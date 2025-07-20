package Item;

public class ClienteItem {
        private String id;
        private String nombre;

        public ClienteItem(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public String getId() {
            return id;
        }
        public String toString() {
            return nombre; // Mostrará el nombre en el combo
        }
    }

