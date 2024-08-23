package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe para gerenciar a conexão com o banco de dados MySQL.
 * <p>
 * Contém as informações de conexão e um metodo para obter uma instância de {@link Connection}.
 * </p>
 *
 * <p>Exemplo de uso:</p>
 * <pre>
 *   try {
 *       Connection conn = MysqlDBConnection.getConnection();
 *       // Use a conexão
 *   } catch (SQLException e) {
 *       e.printStackTrace();
 *   }
 * </pre>
 */
public class MysqlDBConnection {

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
     * @return Uma instância de {@link Connection}.
     * @throws SQLException Se ocorrer um erro ao estabelecer a conexão.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

