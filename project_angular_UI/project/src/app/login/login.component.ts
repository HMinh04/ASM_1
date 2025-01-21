import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import Swal from 'sweetalert2';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, RouterModule],
  template: `
    <div class="max-w-sm mx-auto mt-20 p-6 border border-gray-300 rounded-lg shadow-md">
      <h2 class="text-2xl font-semibold mb-6 text-center">Login</h2>
      <form (ngSubmit)="onSubmit()">
        <div class="mb-4">
          <label for="username" class="block text-sm font-medium text-gray-700">Username:</label>
          <input
            type="text"
            id="username"
            name="username"
            [(ngModel)]="loginData.username"
            required
            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div class="mb-6">
          <label for="password" class="block text-sm font-medium text-gray-700">Password:</label>
          <input
            type="password"
            id="password"
            name="password"
            [(ngModel)]="loginData.password"
            required
            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <button
          type="submit"
          class="w-full px-4 py-2 bg-blue-600 text-white font-semibold rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          Login
        </button>
      </form>
      <p class="mt-4 text-center text-sm">
        Don't have an account? <a routerLink="/register" class="text-blue-600 hover:underline cursor-pointer">Register here</a>
      </p>
    </div>
  `
})
export class LoginComponent {
  loginData = {
    username: '',
    password: ''
  };

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  onSubmit() {
    this.http.post<{ data: { access_token: string, refresh_token: string } }>('http://localhost:8080/login', this.loginData)
      .subscribe({
        next: (response) => {
          // Lưu Access Token và Refresh Token vào localStorage
          localStorage.setItem('authToken', response.data.access_token);
          localStorage.setItem('refreshToken', response.data.refresh_token);
          
          // Hiển thị thông báo thành công
          Swal.fire({
            title: 'Success!',
            text: 'Login successful',
            icon: 'success',
            confirmButtonText: 'OK'
          }).then(() => {
            // Điều hướng đến trang dashboard
            // this.router.navigate(['/dashboard']);
          });
        },
        error: (error) => {
          // Xử lý lỗi
          Swal.fire({
            title: 'Error!',
            text: error.error?.message || 'Login failed',
            icon: 'error',
            confirmButtonText: 'OK'
          });
        }
      });
  }
}
