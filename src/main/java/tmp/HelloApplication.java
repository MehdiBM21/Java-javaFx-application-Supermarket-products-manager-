//package tmp;
//
//import javafx.application.Application;
//
//import javafx.fxml.FXML;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class HelloApplication extends Application {
//
//    @FXML
//    private MFXTableView<String> table;
//
//
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        setupTable();
//        setupPaginated();
//
//        table.autosizeColumnsOnInitialization();
//        paginated.autosizeColumnsOnInitialization();
//
//        When.onChanged(paginated.currentPageProperty())
//                .then((oldValue, newValue) -> paginated.autosizeColumns())
//                .listen();
//    }
//
//    private void setupTable() {
//        MFXTableColumn<Person> nameColumn = new MFXTableColumn<>("Name", true, Comparator.comparing(Person::getName));
//        MFXTableColumn<Person> surnameColumn = new MFXTableColumn<>("Surname", true, Comparator.comparing(Person::getSurname));
//        MFXTableColumn<Person> ageColumn = new MFXTableColumn<>("Age", true, Comparator.comparing(Person::getAge));
//
//        nameColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Person::getName));
//        surnameColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Person::getSurname));
//        ageColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Person::getAge) {{
//            setAlignment(Pos.CENTER_RIGHT);
//        }});
//        ageColumn.setAlignment(Pos.CENTER_RIGHT);
//
//        table.getTableColumns().addAll(nameColumn, surnameColumn, ageColumn);
//        table.getFilters().addAll(
//                new StringFilter<>("Name", Person::getName),
//                new StringFilter<>("Surname", Person::getSurname),
//                new IntegerFilter<>("Age", Person::getAge)
//        );
//        table.setItems(Model.people);
//    }
//
//    private void setupPaginated() {
//        MFXTableColumn<Device> idColumn = new MFXTableColumn<>("ID", false, Comparator.comparing(Device::getID));
//        MFXTableColumn<Device> nameColumn = new MFXTableColumn<>("Name", false, Comparator.comparing(Device::getName));
//        MFXTableColumn<Device> ipColumn = new MFXTableColumn<>("IP", false, Comparator.comparing(Device::getIP));
//        MFXTableColumn<Device> ownerColumn = new MFXTableColumn<>("Owner", false, Comparator.comparing(Device::getOwner));
//        MFXTableColumn<Device> stateColumn = new MFXTableColumn<>("State", false, Comparator.comparing(Device::getState));
//
//        idColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Device::getID));
//        nameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Device::getName));
//        ipColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Device::getIP) {{
//            setAlignment(Pos.CENTER_RIGHT);
//        }});
//        ownerColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Device::getOwner));
//        stateColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Device::getState));
//        ipColumn.setAlignment(Pos.CENTER_RIGHT);
//
//        paginated.getTableColumns().addAll(idColumn, nameColumn, ipColumn, ownerColumn, stateColumn);
//        paginated.getFilters().addAll(
//                new IntegerFilter<>("ID", Device::getID),
//                new StringFilter<>("Name", Device::getName),
//                new StringFilter<>("IP", Device::getIP),
//                new StringFilter<>("Owner", Device::getOwner),
//                new EnumFilter<>("State", Device::getState, Device.State.class)
//        );
//        paginated.setItems(Model.devices);
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//
//    }
//}