package account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Program {

	private static final String USUARIO = "root";
    private static final String SENHA = "root";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/tabajara";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

 

    static MecanismoPersistenciaBDR m;
    static RepositorioContas r;

 

    public static void main(String[] args) {
        try  {
            m = new MecanismoPersistenciaBDR(URL, USUARIO, SENHA, DRIVER);
            r = new RepositorioContasMySQL(m);
        }
        catch (Exception e)  {
            System.out.println("deu bronca");
            e.printStackTrace();
        }

 

        int opcao = 0;
        Scanner s = new Scanner(System.in);
        while (opcao != 9)  {
            System.out.println("\nSistema Bancário Tabajara\n");
            System.out.println("Digite a sua opção:");
            System.out.println("1 - Cadastrar Conta");
            System.out.println("2 - Consultar Conta");
            System.out.println("3 - Alterar Conta");
            System.out.println("4 - Remover Conta");

 

            System.out.println("9 - Sair do sistema");
            System.out.print("Sua opção: ");
            opcao = s.nextInt();

 

            switch (opcao)  {
            case 1: // cadastrar conta
                inserirConta();
                break;

 

            case 2: // consultar conta
                consultarConta();
                break;
            case 3: // alterar conta
                alterarConta();
                break;
            case 4: // remover conta
                removerConta();
                break;
            }
            try {     Thread.sleep(4000);  }

 

            catch(Exception e)  {   e.printStackTrace(); }
        }
        // encerramento do programa...
        System.out.println("Tchau...");
    }

 

    public static void consultarConta()  {
        Scanner s = new Scanner(System.in);
        System.out.println("Digite o número da conta:");
        String numero = s.next();
        try  {
            Conta c = r.procurar(numero);
            if (c != null) {
                System.out.println("Dados da conta: ");
                System.out.println("Numero da conta: " + c.getNumero());
                System.out.println("Nome do cliente: " + c.getNome());
                System.out.println("Saldo da conta : " + c.getSaldo());

 

            } 
        }
        catch(Exception e)  {
            System.out.println("Conta não localizada ou banco fora do ar");
        }

 

    }

 

    public static void inserirConta()  {
        String numero = "", nome = "";
        Scanner s = new Scanner(System.in);
        System.out.println("Digite o número da conta:");
        numero = s.nextLine();
        System.out.println("Digite o nome do cliente dono da conta:");
        nome = s.nextLine();
        System.out.println("Digite o saldo inicial da conta:");
        double saldo = s.nextDouble();
        Conta c = new Conta(numero, nome, saldo);
        try  {
            r.inserir(c);  
            System.out.println("conta inserida com sucesso.");
        }
        catch(Exception e)  {
            System.out.println("A conta não pôde ser inserida, o repositório pode estar cheio ou essa conta já existe no sistema");
        }
    }

 

 

    public static void alterarConta()  {
        String numero = "", nome = "";
        Scanner s = new Scanner(System.in);
        System.out.println("Digite o número da conta:");
        numero = s.nextLine();
        System.out.println("Digite o nome do cliente dono da conta:");
        nome = s.nextLine();
        System.out.println("Digite o saldo inicial da conta:");
        double saldo = s.nextDouble();
        Conta c = new Conta(numero, nome, saldo);
        try  {
            r.atualizar(c);  
            System.out.println("conta atualizada com sucesso.");
        }
        catch(Exception e)  {
            System.out.println("A conta não pôde ser atualizada, a conta é inválida, não existe no sistema ou o banco está fora do ar");
        }

 

    }            

 

 

    public static void removerConta()  {
        Scanner s = new Scanner(System.in);
        System.out.println("Digite o número da conta:");
        String numero = s.next();
        try  {
            r.remover(numero);
            System.out.println("Conta removida.");
        }
        catch(Exception e)  {
            System.out.println("A conta não pôde ser removida, a conta é inválida, não existe no sistema ou o banco está fora do ar");
        }
    }

 


}

 

 


class ContaNaoCadastradaException extends Exception {

 

    public ContaNaoCadastradaException() {
        super("Conta nao cadastrada");
    }
    public ContaNaoCadastradaException(String s) {
        super(s);
    }
}

 


class Conta  {

 

    private String numero;
    private String nome;
    private double saldo;

 

    public Conta(String numero, String nome, double saldo) {
        super();
        this.numero = numero;
        this.nome = nome;
        this.saldo = saldo;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public double getSaldo() {
        return saldo;
    }
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

 

}

 


class RepositorioException extends Exception {

 

    private Exception exception;
    public RepositorioException(Exception ex) {
        exception = ex;
    }
    public void printStackTrace() {
        if (exception != null) {
            exception.printStackTrace();
        }
    }
    public String getMessage() {
        String resposta = "";
        if (exception != null) {
            resposta = exception.getMessage();
        }
        return resposta;
    }
}

 

class TransacaoBDException extends Exception {
    private Exception exception;

 

    public TransacaoBDException(Exception ex) {
        this(ex, "");
    }
    public TransacaoBDException(String mensagem) {
        this(null, mensagem);
    }
    public TransacaoBDException(Exception ex, String mensagem) {
        super(mensagem);
        exception = ex;
    }
    public void printStackTrace() {
        super.printStackTrace();
        if (exception != null) {
            exception.printStackTrace();
        }
    }
    public String getMessage() {
        String resposta = super.getMessage();
        if (exception != null) {
            resposta = resposta + "\n" + exception.getMessage();
        }
        return resposta;
    }
}

 


interface MecanismoPersistencia  {

 

    public void conectar() throws TransacaoBDException;

 

    public void desconectar() throws TransacaoBDException;

 

    public void iniciarTransacao() throws TransacaoBDException;

 

    public void confirmarTransacao() throws TransacaoBDException;

 

    public void cancelarTransacao() throws TransacaoBDException;
}

 

interface RepositorioContas  {
    public void inserir(Conta conta) throws RepositorioException;

 

    public void remover(String s) throws RepositorioException, 
    ContaNaoCadastradaException;

 

    public Conta procurar(String s) throws RepositorioException, 
    ContaNaoCadastradaException;

 

    public boolean existe(String s) throws RepositorioException;

 

    public void atualizar(Conta conta) throws RepositorioException, ContaNaoCadastradaException;
}

 

class MecanismoPersistenciaBDR 
implements MecanismoPersistencia {

 

    private Connection conexao;
    private String classeDoDriver;
    private String url;
    private String login;
    private String senha;

 

    public MecanismoPersistenciaBDR(String url, String login, String senha, String classeDoDriver) throws TransacaoBDException {
        this.classeDoDriver = classeDoDriver;
        this.url = url;
        this.login = login;
        this.senha = senha;

 

    }

 

    public synchronized void conectar() throws TransacaoBDException {
        try {
            if (login != null) 
                conexao = DriverManager.getConnection(url, login, senha);
            else
                conexao = DriverManager.getConnection(url);
        }
        catch (Exception e) {
            throw new TransacaoBDException(e, "EXC_CONECTAR");
        }

 

    }

 

    public synchronized void desconectar() throws TransacaoBDException {
        try {
            conexao.close();     
        }
        catch (Exception e) {
            throw new TransacaoBDException(e, "EXC_DESCONECTAR");
        }
    }

 

    public synchronized void iniciarTransacao() throws TransacaoBDException {
        try {
            conexao.setAutoCommit(false);
        }
        catch (Exception e) {
            throw new TransacaoBDException(e, "EXC_INICIAR_TRANSACAO");
        }
    }

 


    public synchronized void confirmarTransacao() throws TransacaoBDException {
        try {
            conexao.commit();
        }
        catch (Exception e) {
            throw new TransacaoBDException(e, "EXC_CONFIRMAR_TRANSACAO");
        }
    }

 

    public synchronized void cancelarTransacao() throws TransacaoBDException {
        try {
            conexao.rollback();
        }
        catch (Exception e) {
            throw new TransacaoBDException(e, "EXC_CANCELAR_TRANSACAO");
        }
    }
    public synchronized Object getCanalComunicacao() {
        return conexao;
    }   }

 

class RepositorioContasMySQL implements RepositorioContas {

 

    private MecanismoPersistenciaBDR mecanismoPersistencia;

 

    public RepositorioContasMySQL(MecanismoPersistenciaBDR mecanismoPersistencia) {
        this.mecanismoPersistencia = mecanismoPersistencia;
        try  {
            mecanismoPersistencia.conectar();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

 

    public void inserir(Conta conta) throws RepositorioException {
        if (conta == null) {
            throw new IllegalArgumentException("Conta inválida");
        }
        Connection con = null;
        try {
            String query = "INSERT INTO CONTA (numero, nome_cliente, saldo) VALUES('" + conta.getNumero() + "' , '" + conta.getNome() + "' , " + conta.getSaldo() + ")";
            con = (Connection) mecanismoPersistencia.getCanalComunicacao();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        }
        catch (SQLException ex) {
            throw new RepositorioException(ex);
        }
    }

 

    public boolean existe(String numero) throws RepositorioException {
        if (numero == null) {
            throw new IllegalArgumentException("Conta inválida");
        }
        boolean resposta = false;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM CONTA WHERE numero='" + numero + "'";
            con = (Connection)mecanismoPersistencia.getCanalComunicacao();
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            resposta = rs.next();
        }
        catch (SQLException ex) {
            throw new RepositorioException(ex);
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (Exception exception1) {     }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                }
                catch (Exception exception2) { }
            }
        }
        return resposta;
    }
    public Conta procurar(String numero) throws RepositorioException, ContaNaoCadastradaException {
        if (numero == null) {
            throw new IllegalArgumentException("Conta inválida");
        }
        Connection con = null;
        Statement stmt = null;
        Conta resposta = null;
        ResultSet rs = null;
        try {
            String query = "SELECT numero, nome_cliente , saldo FROM CONTA WHERE numero='" + numero + "'";

 

            con = (Connection) mecanismoPersistencia.getCanalComunicacao();
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                resposta = new Conta(rs.getString("numero"),rs.getString("nome_cliente"),rs.getDouble("saldo") );
            } else {
                throw new ContaNaoCadastradaException();
            }
        }
        catch (SQLException ex) {
            throw new RepositorioException(ex);
        }
        catch (Exception ex) {
            throw new RepositorioException(ex);
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (Exception exception1) { }
            }

 

            if (stmt != null) {
                try {
                    stmt.close();
                }
                catch (Exception exception2) {       }
            }
        }
        return resposta;
    }

 

    public void atualizar(Conta conta) throws RepositorioException, ContaNaoCadastradaException {
        if (conta == null) {
            throw new IllegalArgumentException("Conta inválida");
        }
        Connection con = null;
        try {
            String query = "UPDATE CONTA SET nome_cliente='" + conta.getNome() + "'" + ", saldo = " + conta.getSaldo() + " WHERE " + "numero ='" + conta.getNumero() + "'" + "";
            con = (Connection)mecanismoPersistencia.getCanalComunicacao();
            Statement stmt = con.createStatement();
            int numLinhas = stmt.executeUpdate(query);
            stmt.close();
            if (numLinhas == 0) {
                if (!existe(conta.getNumero()) ) {
                    throw new ContaNaoCadastradaException();
                }
            }
        }
        catch (SQLException ex) {
            throw new RepositorioException(ex);
        }
    }

 

    public void remover(String numero) throws RepositorioException, ContaNaoCadastradaException {
        if (numero == null) {
            throw new IllegalArgumentException("Conta inválida");
        }
        Connection con = null;
        try {
            String query = "DELETE FROM CONTA WHERE numero ='" + numero + "'";
            con = (Connection)mecanismoPersistencia.getCanalComunicacao();
            Statement stmt = con.createStatement();
            int numLinhas = stmt.executeUpdate(query);

 

            stmt.close();
            if (numLinhas == 0) {
                throw new ContaNaoCadastradaException();
            }
        }
        catch (SQLException ex) {
            throw new RepositorioException(ex);
        }

 

    }
}       


