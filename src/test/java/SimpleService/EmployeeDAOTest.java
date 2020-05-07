package SimpleService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeDAOTest {

    @Mock
    DriverManager mockDataSource;
    @Mock
    Connection mockConn;
    @Mock
    PreparedStatement mockPreparedStmnt;


    @BeforeAll
    public void setUp() throws SQLException {
        when(mockDataSource.getConnection(anyString())).thenReturn(mockConn);
        doNothing().when(mockConn).commit();
        doNothing().when(mockConn).close();
        when(mockConn.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStmnt);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        doNothing().when(mockPreparedStmnt).setString(anyInt(), anyString());
        when(mockPreparedStmnt.execute()).thenReturn(Boolean.TRUE);
    }

    @Test
    public void connectionByCityNameShouldInvokeGetWeatherMethod() throws ClassNotFoundException{
        // given

        // when

        // then
    }

}
