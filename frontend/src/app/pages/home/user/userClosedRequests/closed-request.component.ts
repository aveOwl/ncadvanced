import {Component, OnInit} from "@angular/core";
import {AuthService} from "../../../../service/auth.service";
import {EmployeeService} from "../../../../service/employee.service";
import {User} from "../../../../model/user.model";
import {Request} from "../../../../model/request.model";


@Component({
  selector: 'user-home',
  templateUrl: 'closed-request.component.html',
  styleUrls: ['closed-request.component.css']
})
export class ClosedRequest implements OnInit {
  private loaded: boolean = false;
  private requests: Request[];
  private selected: Set<number> = new Set();
  private currentUser: User;
  private totalItems: number = 20;

  pageSize: number = 20;

  constructor(private authService: AuthService,
              private employeeService: EmployeeService) {
  }

  settings = {
    delete: false,
    add: false,
    info: true,
    multiSelect: true,
    filterRow: true,
    assign: false,
    join: false,
    reopen: true,
    close: false,
    ajax: false,
    columns: {
      title: true,
      estimate: true,
      dateOfCreation: true,
      priorityStatus: true,
      progressStatus: true,
      reporter: true,
      assignee: true,
    }
}


  ngOnInit() {
    this.authService.currentUser.subscribe(u => {
      this.currentUser = u;
      this.employeeService.getClosedRequestsByReporter(u.id, 1, this.pageSize).subscribe(requests => {
        this.requests = requests;
        this.employeeService.countClosedRequestsByReporter(u.id).subscribe(count => {
          this.totalItems = count;
          this.loaded = true;
        })
      });
    })
  }

  pageChange(data) {
    this.employeeService.getClosedRequestsByReporter(this.currentUser.id, data.page, this.pageSize).subscribe(requests => {
      this.requests = requests;
    })
  }

  select(data) {
    this.selected = data;
  }

  reopen() {
    let sel = Array.from(this.selected);
    //console.log("trying reopen")
    this.employeeService.reopenRequests(sel).subscribe(
      (success) => {
        this.requests = this.requests.map(r => r).filter(r => !this.selected.has(r.id))
        this.selected.clear();
    })
  }

  perChangeLoad(pageData) {
    this.pageSize = pageData.size;
    this.employeeService.getClosedRequestsByReporter(this.currentUser.id, pageData.page, pageData.size).subscribe(requests => {
      this.requests = requests;
    })
  }
}
