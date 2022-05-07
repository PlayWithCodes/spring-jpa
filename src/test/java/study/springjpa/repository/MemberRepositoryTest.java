package study.springjpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.springjpa.dto.MemberDto;
import study.springjpa.entity.Member;
import study.springjpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    public void testMember() {
        System.out.println("memberRepository = " + memberRepository.getClass());
        //given
        Member member = new Member("memberA");
        //when
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        //then
        assertThat(findMember1).isEqualTo(findMember1);
        assertThat(findMember2).isEqualTo(findMember2);
        List<Member> all = memberRepository.findAll();

        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> member = memberRepository.findByUsernameAndAgeGreaterThan("BBB", 10);

        //then
        assertThat(member.get(0).getUsername()).isEqualTo("BBB");
        assertThat(member.get(0).getAge()).isEqualTo(20);
        assertThat(member.size()).isEqualTo(1);
    }

    @Test
    public void findUser() {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> members = memberRepository.findUser("AAA", 10);

        //then
        assertThat(members.get(0).getUsername()).isEqualTo("AAA");
        assertThat(members.get(0).getAge()).isEqualTo(10);
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    public void findUsernameList() {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<String> members = memberRepository.findUsernameList();

        //then
        for (String s : members) {
            System.out.println("s = " + s);
        }
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    public void findMemberDto() {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        Team team = new Team("teamA");
        teamRepository.save(team);

        member1.setTeam(team);
        member2.setTeam(team);

        memberRepository.save(member1);
        memberRepository.save(member2);


        //when
        List<MemberDto> members = memberRepository.findMemberDto();

        //then
        for (MemberDto memberDto : members) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void findByNames() {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> members = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        //then
        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType() {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> listMember = memberRepository.findListByUsername("AAA");
        Member member = memberRepository.findMemberByUsername("BBB");
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAA");

        //then
        for (Member m : listMember) {
            System.out.println("m = " + m);
        }
        System.out.println("member = " + member);
        System.out.println("optional member = " + optionalMember);
    }

    @Test
    public void paging() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //when
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        //then
        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);
    }

    @Test
    public void slicing() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Slice<Member> slice = memberRepository.findSlicingByAge(age, pageRequest);

        //when
        List<Member> content = slice.getContent();

        //then
        assertThat(content.size()).isEqualTo(3);
        assertThat(slice.getNumber()).isEqualTo(0);
        assertThat(slice.isFirst()).isTrue();
        assertThat(slice.hasNext()).isTrue();

        for (Member member : content) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void bulkUpdate() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 30));
        memberRepository.save(new Member("member4", 40));
        memberRepository.save(new Member("member5", 50));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);
        Member member5 = memberRepository.findMemberByUsername("member5");
        System.out.println("member5 = " + member5);

        //then
        assertThat(resultCount).isEqualTo(4);
    }

    @Test
    public void findMemberLazy() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();
        List<Member> members = memberRepository.findAll();
        //when
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.team.class = " + member.getTeam().getClass());
            System.out.println("member.team.name = " + member.getTeam().getName());
        }
        //then
    }

    @Test
    public void findMemberfetchLazy() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();
        List<Member> members = memberRepository.findMemberFetchJoin();
        //when
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.team.class = " + member.getTeam().getClass());
            System.out.println("member.team.name = " + member.getTeam().getName());
        }
        //then
    }

    @Test
    public void findMemberEntityGraph() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();
        List<Member> members = memberRepository.findMemberEntityGraph();
        //when
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.team.class = " + member.getTeam().getClass());
            System.out.println("member.team.name = " + member.getTeam().getName());
        }
        //then
    }

    @Test
    public void findEntityGraphByUsername() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");
        //when
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.team.class = " + member.getTeam().getClass());
            System.out.println("member.team.name = " + member.getTeam().getName());
        }
        //then
    }

    @Test
    public void queryHint() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        entityManager.flush();
        entityManager.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        entityManager.flush();

        //then
    }

    @Test
    public void lock() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        entityManager.flush();
        entityManager.clear();

        //when
        List<Member> result = memberRepository.findLockByUsername("member1");
    }

    @Test
    public void callCustom() {
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }

    @Test
    public void queryByExample() {
        //given
        Team teamA = new Team("teamA");
        entityManager.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        entityManager.persist(m1);
        entityManager.persist(m2);

        entityManager.flush();
        entityManager.clear();

        //when
        Member member = new Member("m1");
        member.setTeam(teamA);

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age");
        Example<Member> example = Example.of(member, matcher);
        List<Member> result = memberRepository.findAll(example);

        //then
        assertThat(result.get(0).getUsername()).isEqualTo("m1");
    }

    @Test
    public void projections() {
        //given
        Team teamA = new Team("teamA");
        entityManager.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        entityManager.persist(m1);
        entityManager.persist(m2);

        entityManager.flush();
        entityManager.clear();

        //when
        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1");
        List<UsernameOnlyDto> resultDto = memberRepository.findDtoProjectionsByUsername("m1", UsernameOnlyDto.class);
        List<NestedClosedProjections> resultNested = memberRepository.findDtoProjectionsByUsername("m1", NestedClosedProjections.class);

        //then
        for (UsernameOnly usernameOnly : result) {
            System.out.println("usernameOnly = " + usernameOnly.getUsername());
        }

        for (UsernameOnlyDto usernameOnlyDto : resultDto) {
            System.out.println("usernameOnlyDto = " + usernameOnlyDto.getUsername());
        }

        for (NestedClosedProjections nested : resultNested) {
            System.out.println("nested = " + nested.getUsername());
            System.out.println("nested = " + nested.getTeam());
        }
    }

    @Test
    public void nativeQuery() {
        //given
        Team teamA = new Team("teamA");
        entityManager.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        entityManager.persist(m1);
        entityManager.persist(m2);

        entityManager.flush();
        entityManager.clear();

        //when
        Member result = memberRepository.findByNativeQuery("m1");

        //then
        System.out.println("result = " + result);
    }

    @Test
    public void nativeProjection() {
        //given
        Team teamA = new Team("teamA");
        entityManager.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        entityManager.persist(m1);
        entityManager.persist(m2);

        entityManager.flush();
        entityManager.clear();

        //when
        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        List<MemberProjection> content = result.getContent();

        //then
        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection.getUsername() = " + memberProjection.getUsername());
            System.out.println("memberProjection.getTeamName() = " + memberProjection.getTeamName());
        }
    }
}