import { useEffect, useState } from "react";
import api from "../services/api";

function Dashboard() {
  const [accounts, setAccounts] = useState([]);
  const [loading, setLoading] = useState(true);

  const [showCreate, setShowCreate] = useState(false);
  const [showDeposit, setShowDeposit] = useState(false);
  const [showWithdraw, setShowWithdraw] = useState(false);
  const [showTransfer, setShowTransfer] = useState(false);
  const [showTransactions, setShowTransactions] = useState(false);
  const [showClose, setShowClose] = useState(false);

  const [selectedAccount, setSelectedAccount] = useState(null);

  const [accountType, setAccountType] = useState("SAVINGS");
  const [depositAmount, setDepositAmount] = useState("");
  const [withdrawAmount, setWithdrawAmount] = useState("");
  const [receiverAccountNumber, setReceiverAccountNumber] = useState("");
  const [transferAmount, setTransferAmount] = useState("");

  const [transactions, setTransactions] = useState([]);
  const [transactionLoading, setTransactionLoading] = useState(false);

  const [processing, setProcessing] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  // =========================
  // LOAD ACCOUNTS
  // =========================

  const fetchAccounts = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await api.get("/api/accounts");

      setAccounts(response.data);
    } catch (err) {
      console.error("Load accounts error:", err);

      setError(
        err.response?.data?.message ||
          "Unable to load accounts."
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAccounts();
  }, []);

  // =========================
  // CREATE ACCOUNT
  // =========================

  const createAccount = async (e) => {
    e.preventDefault();

    try {
      setProcessing(true);
      setError("");
      setSuccess("");

      await api.post("/api/accounts", {
        accountType: accountType,
      });

      setShowCreate(false);

      setSuccess("Account created successfully!");

      await fetchAccounts();
    } catch (err) {
      console.error("Create account error:", err);

      setError(
        err.response?.data?.message ||
          "Unable to create account."
      );
    } finally {
      setProcessing(false);
    }
  };

  // =========================
  // DEPOSIT
  // =========================

  const deposit = async (e) => {
    e.preventDefault();

    const amount = Number(depositAmount);

    if (!selectedAccount) {
      setError("No account selected.");
      return;
    }

    if (amount <= 0) {
      setError("Amount must be greater than 0.");
      return;
    }

    try {
      setProcessing(true);
      setError("");
      setSuccess("");

      await api.post(
        `/api/accounts/${selectedAccount.accountNumber}/deposit`,
        {
          amount: amount,
        }
      );

      setShowDeposit(false);
      setDepositAmount("");

      setSuccess("Amount deposited successfully!");

      await fetchAccounts();
    } catch (err) {
      console.error("Deposit error:", err);

      setError(
        err.response?.data?.message ||
          "Unable to deposit amount."
      );
    } finally {
      setProcessing(false);
    }
  };

  // =========================
  // WITHDRAW
  // =========================

  const withdraw = async (e) => {
    e.preventDefault();

    const amount = Number(withdrawAmount);

    if (!selectedAccount) {
      setError("No account selected.");
      return;
    }

    if (amount <= 0) {
      setError("Amount must be greater than 0.");
      return;
    }

    if (amount > Number(selectedAccount.balance)) {
      setError("Insufficient balance.");
      return;
    }

    try {
      setProcessing(true);
      setError("");
      setSuccess("");

      await api.post(
        `/api/accounts/${selectedAccount.accountNumber}/withdraw`,
        {
          amount: amount,
        }
      );

      setShowWithdraw(false);
      setWithdrawAmount("");

      setSuccess("Amount withdrawn successfully!");

      await fetchAccounts();
    } catch (err) {
      console.error("Withdraw error:", err);

      setError(
        err.response?.data?.message ||
          "Unable to withdraw amount."
      );
    } finally {
      setProcessing(false);
    }
  };

  // =========================
  // TRANSFER
  // =========================

  const transfer = async (e) => {
    e.preventDefault();

    const amount = Number(transferAmount);
    const receiver = receiverAccountNumber.trim();

    if (!selectedAccount) {
      setError("No sender account selected.");
      return;
    }

    if (!receiver) {
      setError("Receiver account number is required.");
      return;
    }

    if (
      receiver ===
      String(selectedAccount.accountNumber)
    ) {
      setError("You cannot transfer to the same account.");
      return;
    }

    if (amount <= 0) {
      setError("Amount must be greater than 0.");
      return;
    }

    if (amount > Number(selectedAccount.balance)) {
      setError("Insufficient balance.");
      return;
    }

    try {
      setProcessing(true);
      setError("");
      setSuccess("");

      const response = await api.post(
        `/api/accounts/${selectedAccount.accountNumber}/transfer`,
        {
          receiverAccountNumber: receiver,
          amount: amount,
        }
      );

      console.log(
        "Transfer response:",
        response.data
      );

      setShowTransfer(false);
      setReceiverAccountNumber("");
      setTransferAmount("");

      setSuccess("Money transferred successfully!");

      await fetchAccounts();
    } catch (err) {
      console.error("Transfer error:", err);

      setError(
        err.response?.data?.message ||
          "Unable to transfer money."
      );
    } finally {
      setProcessing(false);
    }
  };

  // =========================
  // TRANSACTION HISTORY
  // =========================

  const fetchTransactions = async (account) => {
    try {
      setTransactionLoading(true);
      setError("");
      setSuccess("");

      const response = await api.get(
        `/api/accounts/${account.accountNumber}/transactions`
      );

      console.log(
        "Transaction history:",
        response.data
      );

      setTransactions(response.data);
      setSelectedAccount(account);
      setShowTransactions(true);
    } catch (err) {
      console.error(
        "Transaction history error:",
        err
      );

      setError(
        err.response?.data?.message ||
          "Unable to load transaction history."
      );
    } finally {
      setTransactionLoading(false);
    }
  };

  // =========================
  // CLOSE ACCOUNT
  // =========================

  const closeAccount = async () => {
    if (!selectedAccount) {
      setError("No account selected.");
      return;
    }

    if (Number(selectedAccount.balance) !== 0) {
      setError(
        "Account balance must be ₹0.00 before closing."
      );
      return;
    }

    try {
      setProcessing(true);
      setError("");
      setSuccess("");

      await api.patch(
        `/api/accounts/${selectedAccount.accountNumber}/close`
      );

      setShowClose(false);
      setSelectedAccount(null);

      setSuccess("Account closed successfully!");

      await fetchAccounts();
    } catch (err) {
      console.error(
        "Close account error:",
        err
      );

      setError(
        err.response?.data?.message ||
          "Unable to close account."
      );
    } finally {
      setProcessing(false);
    }
  };

  // =========================
  // LOGOUT
  // =========================

  const logout = () => {
    localStorage.removeItem("token");
    window.location.reload();
  };

  // =========================
  // LOADING
  // =========================

  if (loading) {
    return (
      <div className="min-h-screen bg-slate-950 text-white flex items-center justify-center">

        <p className="text-slate-400">
          Loading your accounts...
        </p>

      </div>
    );
  }

  // =========================
  // DASHBOARD
  // =========================

  return (
    <div className="min-h-screen bg-slate-950 text-white">

      {/* =========================
          HEADER
      ========================== */}

      <header className="border-b border-slate-800 bg-slate-900">

        <div className="max-w-7xl mx-auto px-6 py-4 flex justify-between items-center">

          <div className="flex items-center gap-3">

            <div className="h-10 w-10 rounded-xl bg-blue-600 flex items-center justify-center">

              <span className="font-bold">
                F
              </span>

            </div>

            <div>

              <h1 className="text-xl font-bold">
                FinEdge
              </h1>

              <p className="text-xs text-slate-500">
                Secure Banking
              </p>

            </div>

          </div>

          <button
            onClick={logout}
            className="border border-slate-700 rounded-lg px-4 py-2 text-sm hover:bg-slate-800"
          >
            Logout
          </button>

        </div>

      </header>


      {/* =========================
          MAIN
      ========================== */}

      <main className="max-w-7xl mx-auto px-6 py-8">

        <div className="flex justify-between items-center mb-8">

          <div>

            <h2 className="text-3xl font-bold">
              Dashboard
            </h2>

            <p className="text-slate-400 mt-2">
              Manage your FinEdge accounts and transactions.
            </p>

          </div>

          <button
            onClick={() => {
              setShowCreate(true);
              setError("");
              setSuccess("");
            }}
            className="bg-blue-600 hover:bg-blue-500 rounded-xl px-5 py-3 font-semibold"
          >
            + Create Account
          </button>

        </div>


        {/* =========================
            SUCCESS MESSAGE
        ========================== */}

        {success && (

          <div className="mb-6 bg-green-500/10 border border-green-500/20 text-green-400 rounded-xl px-5 py-4">

            {success}

          </div>

        )}


        {/* =========================
            ERROR MESSAGE
        ========================== */}

        {error && (

          <div className="mb-6 bg-red-500/10 border border-red-500/20 text-red-400 rounded-xl px-5 py-4">

            {error}

          </div>

        )}


        {/* =========================
            ACCOUNTS
        ========================== */}

        {accounts.length === 0 ? (

          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-10 text-center">

            <h3 className="text-xl font-semibold">
              No accounts found
            </h3>

            <p className="text-slate-400 mt-2">
              Create your first FinEdge account.
            </p>

            <button
              onClick={() => setShowCreate(true)}
              className="mt-6 bg-blue-600 hover:bg-blue-500 rounded-xl px-6 py-3 font-semibold"
            >
              Create Account
            </button>

          </div>

        ) : (

          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">

            {accounts.map((account) => (

              <div
                key={account.accountNumber}
                className="bg-slate-900 border border-slate-800 rounded-2xl p-6"
              >

                {/* ACCOUNT HEADER */}

                <div className="flex justify-between">

                  <div>

                    <p className="text-sm text-slate-500">
                      Available Balance
                    </p>

                    <h3 className="text-3xl font-bold mt-2">

                      ₹
                      {Number(
                        account.balance
                      ).toLocaleString(
                        "en-IN",
                        {
                          minimumFractionDigits: 2,
                          maximumFractionDigits: 2,
                        }
                      )}

                    </h3>

                  </div>

                  <span className="bg-blue-600/10 text-blue-400 px-3 py-2 rounded-xl text-sm h-fit">
                    {account.accountType}
                  </span>

                </div>


                {/* ACCOUNT DETAILS */}

                <div className="border-t border-slate-800 mt-6 pt-5 space-y-4">

                  <div className="flex justify-between">

                    <span className="text-slate-500">
                      Account Number
                    </span>

                    <span>
                      {account.accountNumber}
                    </span>

                  </div>


                  <div className="flex justify-between">

                    <span className="text-slate-500">
                      Account Holder
                    </span>

                    <span>
                      {account.accountHolder}
                    </span>

                  </div>


                  <div className="flex justify-between">

                    <span className="text-slate-500">
                      Status
                    </span>

                    <span
                      className={
                        account.status === "ACTIVE"
                          ? "text-green-400"
                          : "text-red-400"
                      }
                    >
                      {account.status}
                    </span>

                  </div>

                </div>


                {/* =========================
                    ACTION BUTTONS
                ========================== */}

                <div className="grid grid-cols-2 md:grid-cols-5 gap-2 mt-6">

                  {/* DEPOSIT */}

                  <button
                    disabled={
                      account.status !== "ACTIVE"
                    }
                    onClick={() => {

                      setSelectedAccount(account);
                      setShowDeposit(true);
                      setDepositAmount("");
                      setError("");
                      setSuccess("");

                    }}
                    className="bg-blue-600 hover:bg-blue-500 disabled:opacity-40 disabled:cursor-not-allowed rounded-lg py-2 text-sm"
                  >
                    Deposit
                  </button>


                  {/* WITHDRAW */}

                  <button
                    disabled={
                      account.status !== "ACTIVE"
                    }
                    onClick={() => {

                      setSelectedAccount(account);
                      setShowWithdraw(true);
                      setWithdrawAmount("");
                      setError("");
                      setSuccess("");

                    }}
                    className="bg-slate-800 hover:bg-slate-700 disabled:opacity-40 disabled:cursor-not-allowed rounded-lg py-2 text-sm"
                  >
                    Withdraw
                  </button>


                  {/* TRANSFER */}

                  <button
                    disabled={
                      account.status !== "ACTIVE"
                    }
                    onClick={() => {

                      console.log(
                        "TRANSFER CLICKED"
                      );

                      setSelectedAccount(account);
                      setShowTransfer(true);
                      setReceiverAccountNumber("");
                      setTransferAmount("");
                      setError("");
                      setSuccess("");

                    }}
                    className="bg-slate-800 hover:bg-slate-700 disabled:opacity-40 disabled:cursor-not-allowed rounded-lg py-2 text-sm"
                  >
                    Transfer
                  </button>


                  {/* TRANSACTIONS */}

                  <button
                    onClick={() =>
                      fetchTransactions(account)
                    }
                    className="bg-slate-800 hover:bg-slate-700 rounded-lg py-2 text-sm"
                  >
                    Transactions
                  </button>


                  {/* CLOSE */}

                  <button
                    disabled={
                      account.status !== "ACTIVE" ||
                      Number(account.balance) !== 0
                    }
                    onClick={() => {

                      setSelectedAccount(account);
                      setShowClose(true);
                      setError("");
                      setSuccess("");

                    }}
                    className="bg-red-600/20 text-red-400 hover:bg-red-600/30 disabled:opacity-40 disabled:cursor-not-allowed rounded-lg py-2 text-sm"
                  >
                    Close
                  </button>

                </div>

              </div>

            ))}

          </div>

        )}

      </main>


      {/* ==================================================
          CREATE ACCOUNT MODAL
      ================================================== */}

      {showCreate && (

        <div className="fixed inset-0 bg-black/70 flex items-center justify-center px-6 z-50">

          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-7 w-full max-w-md">

            <h3 className="text-xl font-semibold">
              Create Account
            </h3>

            <p className="text-slate-400 text-sm mt-1">
              Choose your account type.
            </p>


            <form
              onSubmit={createAccount}
              className="mt-6 space-y-5"
            >

              <select
                value={accountType}
                onChange={(e) =>
                  setAccountType(
                    e.target.value
                  )
                }
                className="w-full bg-slate-800 border border-slate-700 rounded-xl px-4 py-3"
              >

                <option value="SAVINGS">
                  Savings Account
                </option>

                <option value="CURRENT">
                  Current Account
                </option>

              </select>


              <div className="flex gap-3">

                <button
                  type="button"
                  onClick={() =>
                    setShowCreate(false)
                  }
                  className="flex-1 border border-slate-700 rounded-xl py-3"
                >
                  Cancel
                </button>


                <button
                  type="submit"
                  disabled={processing}
                  className="flex-1 bg-blue-600 hover:bg-blue-500 rounded-xl py-3 font-semibold"
                >
                  {processing
                    ? "Creating..."
                    : "Create"}
                </button>

              </div>

            </form>

          </div>

        </div>

      )}


      {/* ==================================================
          DEPOSIT MODAL
      ================================================== */}

      {showDeposit && (

        <div className="fixed inset-0 bg-black/70 flex items-center justify-center px-6 z-50">

          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-7 w-full max-w-md">

            <h3 className="text-xl font-semibold">
              Deposit Money
            </h3>

            <p className="text-slate-400 text-sm mt-1">
              Add money to your account.
            </p>


            {selectedAccount && (

              <div className="mt-5 bg-slate-950 border border-slate-800 rounded-xl p-4">

                <p className="text-xs text-slate-500">
                  Account
                </p>

                <p className="mt-1">
                  {selectedAccount.accountNumber}
                </p>

                <p className="text-sm text-slate-400 mt-1">
                  Current balance: ₹
                  {Number(
                    selectedAccount.balance
                  ).toLocaleString(
                    "en-IN",
                    {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    }
                  )}
                </p>

              </div>

            )}


            <form
              onSubmit={deposit}
              className="mt-6 space-y-5"
            >

              <input
                type="number"
                min="0.01"
                step="0.01"
                value={depositAmount}
                onChange={(e) =>
                  setDepositAmount(
                    e.target.value
                  )
                }
                placeholder="Enter amount"
                required
                className="w-full bg-slate-800 border border-slate-700 rounded-xl px-4 py-3"
              />


              <div className="flex gap-3">

                <button
                  type="button"
                  onClick={() =>
                    setShowDeposit(false)
                  }
                  className="flex-1 border border-slate-700 rounded-xl py-3"
                >
                  Cancel
                </button>


                <button
                  type="submit"
                  disabled={processing}
                  className="flex-1 bg-blue-600 hover:bg-blue-500 rounded-xl py-3 font-semibold"
                >
                  {processing
                    ? "Depositing..."
                    : "Deposit"}
                </button>

              </div>

            </form>

          </div>

        </div>

      )}


      {/* ==================================================
          WITHDRAW MODAL
      ================================================== */}

      {showWithdraw && (

        <div className="fixed inset-0 bg-black/70 flex items-center justify-center px-6 z-50">

          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-7 w-full max-w-md">

            <h3 className="text-xl font-semibold">
              Withdraw Money
            </h3>


            {selectedAccount && (

              <div className="mt-5 bg-slate-950 border border-slate-800 rounded-xl p-4">

                <p className="text-xs text-slate-500">
                  Available Balance
                </p>

                <p className="text-xl font-semibold mt-1">
                  ₹
                  {Number(
                    selectedAccount.balance
                  ).toLocaleString(
                    "en-IN",
                    {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    }
                  )}
                </p>

              </div>

            )}


            <form
              onSubmit={withdraw}
              className="mt-6 space-y-5"
            >

              <input
                type="number"
                min="0.01"
                step="0.01"
                value={withdrawAmount}
                onChange={(e) =>
                  setWithdrawAmount(
                    e.target.value
                  )
                }
                placeholder="Enter amount"
                required
                className="w-full bg-slate-800 border border-slate-700 rounded-xl px-4 py-3"
              />


              <div className="flex gap-3">

                <button
                  type="button"
                  onClick={() =>
                    setShowWithdraw(false)
                  }
                  className="flex-1 border border-slate-700 rounded-xl py-3"
                >
                  Cancel
                </button>


                <button
                  type="submit"
                  disabled={processing}
                  className="flex-1 bg-blue-600 hover:bg-blue-500 rounded-xl py-3 font-semibold"
                >
                  {processing
                    ? "Withdrawing..."
                    : "Withdraw"}
                </button>

              </div>

            </form>

          </div>

        </div>

      )}


      {/* ==================================================
          TRANSFER MODAL
      ================================================== */}

      {showTransfer && (

        <div className="fixed inset-0 bg-black/70 flex items-center justify-center px-6 z-50">

          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-7 w-full max-w-md">

            <div className="flex justify-between items-center">

              <div>

                <h3 className="text-xl font-semibold">
                  Transfer Money
                </h3>

                <p className="text-slate-400 text-sm mt-1">
                  Send money to another account.
                </p>

              </div>


              <button
                type="button"
                onClick={() =>
                  setShowTransfer(false)
                }
                className="text-2xl text-slate-500 hover:text-white"
              >
                ×
              </button>

            </div>


            {/* SENDER */}

            {selectedAccount && (

              <div className="mt-5 bg-slate-950 border border-slate-800 rounded-xl p-4">

                <p className="text-xs text-slate-500">
                  From Account
                </p>

                <p className="font-medium mt-1">
                  {selectedAccount.accountNumber}
                </p>

                <p className="text-sm text-slate-400 mt-1">
                  Available: ₹
                  {Number(
                    selectedAccount.balance
                  ).toLocaleString(
                    "en-IN",
                    {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    }
                  )}
                </p>

              </div>

            )}


            <form
              onSubmit={transfer}
              className="mt-6 space-y-5"
            >

              {/* RECEIVER */}

              <div>

                <label className="block text-sm text-slate-300 mb-2">
                  Receiver Account Number
                </label>

                <input
                  type="text"
                  value={receiverAccountNumber}
                  onChange={(e) =>
                    setReceiverAccountNumber(
                      e.target.value
                    )
                  }
                  placeholder="Enter receiver account number"
                  required
                  className="w-full bg-slate-800 border border-slate-700 rounded-xl px-4 py-3 text-white"
                />

              </div>


              {/* AMOUNT */}

              <div>

                <label className="block text-sm text-slate-300 mb-2">
                  Amount
                </label>

                <input
                  type="number"
                  min="0.01"
                  step="0.01"
                  value={transferAmount}
                  onChange={(e) =>
                    setTransferAmount(
                      e.target.value
                    )
                  }
                  placeholder="Enter amount"
                  required
                  className="w-full bg-slate-800 border border-slate-700 rounded-xl px-4 py-3 text-white"
                />

              </div>


              {/* BUTTONS */}

              <div className="flex gap-3">

                <button
                  type="button"
                  onClick={() => {

                    setShowTransfer(false);
                    setReceiverAccountNumber("");
                    setTransferAmount("");

                  }}
                  className="flex-1 border border-slate-700 rounded-xl py-3"
                >
                  Cancel
                </button>


                <button
                  type="submit"
                  disabled={processing}
                  className="flex-1 bg-blue-600 hover:bg-blue-500 rounded-xl py-3 font-semibold"
                >
                  {processing
                    ? "Transferring..."
                    : "Transfer"}
                </button>

              </div>

            </form>

          </div>

        </div>

      )}


      {/* ==================================================
          TRANSACTION HISTORY MODAL
      ================================================== */}

      {showTransactions && (

        <div className="fixed inset-0 bg-black/70 flex items-center justify-center px-6 z-50">

          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-7 w-full max-w-2xl max-h-[85vh] overflow-y-auto">

            {/* HEADER */}

            <div className="flex justify-between items-center mb-6">

              <div>

                <h3 className="text-xl font-semibold">
                  Transaction History
                </h3>

                {selectedAccount && (

                  <p className="text-slate-400 text-sm mt-1">
                    Account:{" "}
                    {selectedAccount.accountNumber}
                  </p>

                )}

              </div>


              <button
                type="button"
                onClick={() => {

                  setShowTransactions(false);
                  setTransactions([]);

                }}
                className="text-2xl text-slate-500 hover:text-white"
              >
                ×
              </button>

            </div>


            {/* LOADING */}

            {transactionLoading ? (

              <div className="text-center py-10 text-slate-400">

                Loading transactions...

              </div>

            ) : transactions.length === 0 ? (

              /* EMPTY */

              <div className="text-center py-10">

                <p className="text-slate-300">
                  No transactions yet.
                </p>

                <p className="text-slate-500 text-sm mt-2">
                  Your deposits, withdrawals and transfers
                  will appear here.
                </p>

              </div>

            ) : (

              /* TRANSACTIONS */

              <div className="space-y-3">

                {transactions.map(
                  (transaction) => {

                    const amount =
                      Number(
                        transaction.amount
                      );

                    const isPositive =
                      amount >= 0;

                    return (

                      <div
                        key={transaction.id}
                        className="bg-slate-950 border border-slate-800 rounded-xl p-4"
                      >

                        <div className="flex justify-between items-center">

                          <div>

                            <p className="font-semibold">
                              {transaction.transactionType}
                            </p>

                            <p className="text-xs text-slate-500 mt-1">

                              {new Date(
                                transaction.transactionDate
                              ).toLocaleString(
                                "en-IN"
                              )}

                            </p>

                          </div>


                          <div className="text-right">

                            <p
                              className={
                                isPositive
                                  ? "text-green-400 font-semibold"
                                  : "text-red-400 font-semibold"
                              }
                            >

                              {isPositive
                                ? "+"
                                : "-"}
                              ₹
                              {Math.abs(
                                amount
                              ).toLocaleString(
                                "en-IN",
                                {
                                  minimumFractionDigits: 2,
                                  maximumFractionDigits: 2,
                                }
                              )}

                            </p>


                            <p className="text-xs text-slate-500 mt-1">

                              Balance: ₹
                              {Number(
                                transaction.balanceAfterTransaction
                              ).toLocaleString(
                                "en-IN",
                                {
                                  minimumFractionDigits: 2,
                                  maximumFractionDigits: 2,
                                }
                              )}

                            </p>

                          </div>

                        </div>

                      </div>

                    );

                  }
                )}

              </div>

            )}

          </div>

        </div>

      )}


      {/* ==================================================
          CLOSE ACCOUNT MODAL
      ================================================== */}

      {showClose && selectedAccount && (

        <div className="fixed inset-0 bg-black/70 flex items-center justify-center px-6 z-50">

          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-7 w-full max-w-md">

            <h3 className="text-xl font-semibold">
              Close Account
            </h3>

            <p className="text-slate-400 mt-3">
              Are you sure you want to close this account?
            </p>


            {/* ACCOUNT DETAILS */}

            <div className="mt-5 bg-slate-950 border border-slate-800 rounded-xl p-4">

              <p className="text-xs text-slate-500">
                Account Number
              </p>

              <p className="mt-1 font-medium">
                {selectedAccount.accountNumber}
              </p>


              <p className="text-sm text-slate-400 mt-3">
                Account Type
              </p>

              <p className="mt-1">
                {selectedAccount.accountType}
              </p>


              <p className="text-sm text-slate-400 mt-3">
                Current Balance
              </p>

              <p className="mt-1">
                ₹
                {Number(
                  selectedAccount.balance
                ).toLocaleString(
                  "en-IN",
                  {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2,
                  }
                )}
              </p>

            </div>


            <p className="text-yellow-400 text-sm mt-4">
              The account must have a ₹0.00 balance before
              it can be closed.
            </p>


            {/* BUTTONS */}

            <div className="flex gap-3 mt-6">

              <button
                type="button"
                onClick={() => {

                  setShowClose(false);
                  setSelectedAccount(null);

                }}
                className="flex-1 border border-slate-700 rounded-xl py-3"
              >
                Cancel
              </button>


              <button
                type="button"
                onClick={closeAccount}
                disabled={
                  processing ||
                  Number(
                    selectedAccount.balance
                  ) !== 0
                }
                className="flex-1 bg-red-600 hover:bg-red-500 disabled:opacity-50 disabled:cursor-not-allowed rounded-xl py-3 font-semibold"
              >
                {processing
                  ? "Closing..."
                  : "Close Account"}
              </button>

            </div>

          </div>

        </div>

      )}

    </div>
  );
}

export default Dashboard;