package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Esta classe fornece um metodo estático para obter uma conexão com o banco de dados MySQL.
 * <p>
 * A classe define as constantes para URL de conexão, nome de usuário e senha.
 */
public class MySQLConnection {

    /**
     * A URL de conexão com o banco de dados MySQL.
     */
    private static final String URL = "jdbc:mysql://localhost:3306/db_livraria_solutis";

    /**
     * O nome de usuário para autenticação no banco de dados.
     */
    private static final String USER = "root";

    /**
     * A senha para autenticação no banco de dados.
     */
    private static final String PASSWORD = "GhostSthong567890@";

    /**
     * Retorna uma conexão com o banco de dados MySQL.
     *
     * @return Uma instância de {@link Connection} representando a conexão com o banco de dados.
     * @throws SQLException Se ocorrer um erro ao estabelecer a conexão.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
