import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  IconButton,
  Chip,
  Alert,
  Snackbar
} from '@mui/material';
import { Add, Remove, Edit, History } from '@mui/icons-material';
import axios from 'axios';

const AdminCoinManagement = () => {
  const [userCoins, setUserCoins] = useState([]);
  const [loading, setLoading] = useState(true);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogType, setDialogType] = useState(''); // 'add', 'subtract', 'edit'
  const [selectedUser, setSelectedUser] = useState(null);
  const [amount, setAmount] = useState('');
  const [reason, setReason] = useState('');
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });
  const [transactionDialogOpen, setTransactionDialogOpen] = useState(false);
  const [transactions, setTransactions] = useState([]);

  useEffect(() => {
    fetchUserCoins();
  }, []);

  const fetchUserCoins = async () => {
    try {
      setLoading(true);
      const response = await axios.get('http://localhost:8080/api/admin/user-coins');
      setUserCoins(response.data);
    } catch (error) {
      console.error('Error fetching user coins:', error);
      showSnackbar('Lỗi khi tải danh sách coin', 'error');
    } finally {
      setLoading(false);
    }
  };

  const showSnackbar = (message, severity = 'success') => {
    setSnackbar({ open: true, message, severity });
  };

  const handleOpenDialog = (type, user) => {
    setDialogType(type);
    setSelectedUser(user);
    setAmount('');
    setReason('');
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setSelectedUser(null);
    setAmount('');
    setReason('');
  };

  const handleSubmit = async () => {
    if (!amount || amount <= 0) {
      showSnackbar('Vui lòng nhập số lượng hợp lệ', 'error');
      return;
    }

    try {
      let response;
      const requestData = { amount: parseInt(amount), reason };

      switch (dialogType) {
        case 'add':
          response = await axios.post(
            `http://localhost:8080/api/admin/user-coins/${selectedUser.userID}/add`,
            requestData
          );
          break;
        case 'subtract':
          response = await axios.post(
            `http://localhost:8080/api/admin/user-coins/${selectedUser.userID}/subtract`,
            requestData
          );
          break;
        case 'edit':
          response = await axios.put(
            `http://localhost:8080/api/admin/user-coins/${selectedUser.userID}`,
            { coinAmount: parseInt(amount), reason }
          );
          break;
        default:
          return;
      }

      showSnackbar(`${dialogType === 'add' ? 'Thêm' : dialogType === 'subtract' ? 'Trừ' : 'Cập nhật'} coin thành công!`);
      handleCloseDialog();
      fetchUserCoins();
    } catch (error) {
      console.error('Error updating coins:', error);
      showSnackbar('Lỗi khi cập nhật coin', 'error');
    }
  };

  const handleViewTransactions = async (user) => {
    try {
      const response = await axios.get(`http://localhost:8080/api/admin/user-coins/${user.userID}/transactions`);
      setTransactions(response.data);
      setSelectedUser(user);
      setTransactionDialogOpen(true);
    } catch (error) {
      console.error('Error fetching transactions:', error);
      showSnackbar('Lỗi khi tải lịch sử giao dịch', 'error');
    }
  };

  const getTransactionTypeLabel = (type) => {
    switch (type) {
      case 'admin_add': return 'Admin thêm';
      case 'admin_subtract': return 'Admin trừ';
      case 'bet': return 'Đặt cược';
      case 'win': return 'Thắng cược';
      case 'lose': return 'Thua cược';
      default: return type;
    }
  };

  const getTransactionColor = (type) => {
    switch (type) {
      case 'admin_add':
      case 'win':
        return 'success';
      case 'admin_subtract':
      case 'bet':
      case 'lose':
        return 'error';
      default:
        return 'default';
    }
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <Typography>Đang tải...</Typography>
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Quản lý Coin
      </Typography>

      <Paper sx={{ width: '100%', overflow: 'hidden' }}>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>User ID</TableCell>
                <TableCell>Username</TableCell>
                <TableCell align="right">Số Coin</TableCell>
                <TableCell>Cập nhật lúc</TableCell>
                <TableCell align="center">Thao tác</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {userCoins.map((user) => (
                <TableRow key={user.userID}>
                  <TableCell>{user.userID}</TableCell>
                  <TableCell>{user.username}</TableCell>
                  <TableCell align="right">
                    <Chip 
                      label={`${user.coinAmount.toLocaleString()} coin`}
                      color="primary"
                      variant="outlined"
                    />
                  </TableCell>
                  <TableCell>
                    {new Date(user.updatedAt).toLocaleString('vi-VN')}
                  </TableCell>
                  <TableCell align="center">
                    <IconButton
                      color="primary"
                      onClick={() => handleOpenDialog('add', user)}
                      title="Thêm coin"
                    >
                      <Add />
                    </IconButton>
                    <IconButton
                      color="warning"
                      onClick={() => handleOpenDialog('subtract', user)}
                      title="Trừ coin"
                    >
                      <Remove />
                    </IconButton>
                    <IconButton
                      color="info"
                      onClick={() => handleOpenDialog('edit', user)}
                      title="Sửa số coin"
                    >
                      <Edit />
                    </IconButton>
                    <IconButton
                      color="secondary"
                      onClick={() => handleViewTransactions(user)}
                      title="Xem lịch sử"
                    >
                      <History />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>

      {/* Dialog cho thêm/trừ/sửa coin */}
      <Dialog open={dialogOpen} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>
          {dialogType === 'add' && 'Thêm Coin'}
          {dialogType === 'subtract' && 'Trừ Coin'}
          {dialogType === 'edit' && 'Sửa Số Coin'}
        </DialogTitle>
        <DialogContent>
          <Box sx={{ mt: 2 }}>
            <Typography variant="body2" color="text.secondary" gutterBottom>
              User: <strong>{selectedUser?.username}</strong>
            </Typography>
            <Typography variant="body2" color="text.secondary" gutterBottom>
              Coin hiện tại: <strong>{selectedUser?.coinAmount?.toLocaleString()}</strong>
            </Typography>
            <TextField
              fullWidth
              label="Số lượng"
              type="number"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              sx={{ mt: 2 }}
            />
            <TextField
              fullWidth
              label="Lý do"
              value={reason}
              onChange={(e) => setReason(e.target.value)}
              sx={{ mt: 2 }}
            />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Hủy</Button>
          <Button onClick={handleSubmit} variant="contained">
            {dialogType === 'add' && 'Thêm'}
            {dialogType === 'subtract' && 'Trừ'}
            {dialogType === 'edit' && 'Cập nhật'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Dialog xem lịch sử giao dịch */}
      <Dialog open={transactionDialogOpen} onClose={() => setTransactionDialogOpen(false)} maxWidth="md" fullWidth>
        <DialogTitle>
          Lịch sử giao dịch - {selectedUser?.username}
        </DialogTitle>
        <DialogContent>
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Thời gian</TableCell>
                  <TableCell>Loại</TableCell>
                  <TableCell align="right">Số lượng</TableCell>
                  <TableCell>Số dư sau</TableCell>
                  <TableCell>Mô tả</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {transactions.map((transaction) => (
                  <TableRow key={transaction.transactionID}>
                    <TableCell>
                      {new Date(transaction.createdAt).toLocaleString('vi-VN')}
                    </TableCell>
                    <TableCell>
                      <Chip 
                        label={getTransactionTypeLabel(transaction.transactionType)}
                        color={getTransactionColor(transaction.transactionType)}
                        size="small"
                      />
                    </TableCell>
                    <TableCell align="right">
                      <Typography
                        color={transaction.amount > 0 ? 'success.main' : 'error.main'}
                        fontWeight="bold"
                      >
                        {transaction.amount > 0 ? '+' : ''}{transaction.amount.toLocaleString()}
                      </Typography>
                    </TableCell>
                    <TableCell>
                      {transaction.balanceAfter.toLocaleString()}
                    </TableCell>
                    <TableCell>
                      {transaction.description || '-'}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setTransactionDialogOpen(false)}>Đóng</Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
      >
        <Alert severity={snackbar.severity} onClose={() => setSnackbar({ ...snackbar, open: false })}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default AdminCoinManagement; 